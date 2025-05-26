const { useState, useEffect } = React;

// 브랜드 생성/수정 모달 컴포넌트
const BrandModal = ({ isOpen, onClose, onSave, brand, mode }) => {
    const [name, setName] = useState('');
    const [loading, setLoading] = useState(false);

    useEffect(() => {
        if (brand && mode === 'edit') {
            setName(brand.name);
        } else {
            setName('');
        }
    }, [brand, mode, isOpen]);

    const handleSubmit = async (e) => {
        e.preventDefault();
        if (!name.trim()) {
            alert('브랜드명을 입력해주세요.');
            return;
        }

        setLoading(true);
        try {
            if (mode === 'edit') {
                await brandAPI.update(brand.id, name.trim());
            } else {
                await brandAPI.create(name.trim());
            }
            onSave();
            onClose();
            setName('');
        } catch (error) {
            alert(error.message);
        } finally {
            setLoading(false);
        }
    };

    if (!isOpen) return null;

    return (
        <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50">
            <div className="bg-white rounded-lg p-6 w-96 max-w-90vw">
                <div className="flex justify-between items-center mb-4">
                    <h2 className="text-xl font-bold">
                        {mode === 'edit' ? '브랜드 수정' : '브랜드 추가'}
                    </h2>
                    <button
                        onClick={onClose}
                        className="text-gray-500 hover:text-gray-700"
                    >
                        <i className="fas fa-times"></i>
                    </button>
                </div>
                <form onSubmit={handleSubmit}>
                    <div className="mb-4">
                        <label className="block text-sm font-medium text-gray-700 mb-2">
                            브랜드명
                        </label>
                        <input
                            type="text"
                            value={name}
                            onChange={(e) => setName(e.target.value)}
                            className="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
                            placeholder="브랜드명을 입력하세요"
                            disabled={loading}
                        />
                    </div>
                    <div className="flex justify-end space-x-2">
                        <button
                            type="button"
                            onClick={onClose}
                            className="px-4 py-2 text-gray-600 border border-gray-300 rounded-md hover:bg-gray-50"
                            disabled={loading}
                        >
                            취소
                        </button>
                        <button
                            type="submit"
                            className="px-4 py-2 bg-blue-600 text-white rounded-md hover:bg-blue-700 disabled:opacity-50"
                            disabled={loading}
                        >
                            {loading ? '처리중...' : (mode === 'edit' ? '수정' : '추가')}
                        </button>
                    </div>
                </form>
            </div>
        </div>
    );
};

// 브랜드 카드 컴포넌트
const BrandCard = ({ brand, onEdit, onDelete, onManageProducts }) => {
    const [deleting, setDeleting] = useState(false);

    const handleDelete = async () => {
        if (!confirm(`'${brand.name}' 브랜드를 삭제하시겠습니까?`)) {
            return;
        }

        setDeleting(true);
        try {
            await brandAPI.delete(brand.id);
            onDelete();
        } catch (error) {
            alert(error.message);
        } finally {
            setDeleting(false);
        }
    };

    return (
        <div className="bg-white rounded-lg shadow-md p-6 hover:shadow-lg transition-shadow">
            <div className="flex justify-between items-start mb-4">
                <div className="flex-1">
                    <h3 className="text-lg font-semibold text-gray-900 mb-2">
                        {brand.name}
                    </h3>
                    <p className="text-sm text-gray-500">ID: {brand.id}</p>
                </div>
                <div className="flex space-x-2">
                    <button
                        onClick={() => onEdit(brand)}
                        className="p-2 text-blue-600 hover:bg-blue-50 rounded-md transition-colors"
                        title="수정"
                    >
                        <i className="fas fa-edit"></i>
                    </button>
                    <button
                        onClick={handleDelete}
                        disabled={deleting}
                        className="p-2 text-red-600 hover:bg-red-50 rounded-md transition-colors disabled:opacity-50"
                        title="삭제"
                    >
                        <i className="fas fa-trash"></i>
                    </button>
                </div>
            </div>
            <div className="border-t pt-4">
                <button
                    onClick={() => onManageProducts(brand)}
                    className="w-full bg-green-600 text-white px-4 py-2 rounded-md hover:bg-green-700 flex items-center justify-center space-x-2"
                >
                    <i className="fas fa-box"></i>
                    <span>상품 관리</span>
                </button>
            </div>
        </div>
    );
};

// 메인 브랜드 관리 컴포넌트
const BrandManagement = ({ onManageProducts }) => {
    const [brands, setBrands] = useState([]);
    const [loading, setLoading] = useState(true);
    const [modalOpen, setModalOpen] = useState(false);
    const [modalMode, setModalMode] = useState('create'); // 'create' or 'edit'
    const [selectedBrand, setSelectedBrand] = useState(null);
    const [searchTerm, setSearchTerm] = useState('');

    // 브랜드 목록 로드
    const loadBrands = async () => {
        setLoading(true);
        try {
            const data = await brandAPI.getAll();
            setBrands(data);
        } catch (error) {
            alert(error.message);
        } finally {
            setLoading(false);
        }
    };

    useEffect(() => {
        loadBrands();
    }, []);

    // 브랜드 추가 모달 열기
    const handleAddBrand = () => {
        setModalMode('create');
        setSelectedBrand(null);
        setModalOpen(true);
    };

    // 브랜드 수정 모달 열기
    const handleEditBrand = (brand) => {
        setModalMode('edit');
        setSelectedBrand(brand);
        setModalOpen(true);
    };

    // 모달 닫기
    const handleCloseModal = () => {
        setModalOpen(false);
        setSelectedBrand(null);
    };

    // 브랜드 저장 후 목록 새로고침
    const handleSaveBrand = () => {
        loadBrands();
    };

    // 브랜드 삭제 후 목록 새로고침
    const handleDeleteBrand = () => {
        loadBrands();
    };

    // 검색 필터링
    const filteredBrands = brands.filter(brand =>
        brand.name.toLowerCase().includes(searchTerm.toLowerCase())
    );

    return (
        <div className="min-h-screen bg-gray-50">
            {/* 헤더 */}
            <div className="bg-white shadow-sm">
                <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
                    <div className="flex justify-between items-center py-6">
                        <div>
                            <h1 className="text-3xl font-bold text-gray-900">
                                브랜드 관리
                            </h1>
                            <p className="mt-1 text-sm text-gray-500">
                                브랜드를 추가, 수정, 삭제하고 각 브랜드의 상품을 관리할 수 있습니다.
                            </p>
                        </div>
                        <button
                            onClick={handleAddBrand}
                            className="bg-blue-600 text-white px-4 py-2 rounded-md hover:bg-blue-700 flex items-center space-x-2"
                        >
                            <i className="fas fa-plus"></i>
                            <span>브랜드 추가</span>
                        </button>
                    </div>
                </div>
            </div>

            {/* 메인 컨텐츠 */}
            <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
                {/* 검색 및 통계 */}
                <div className="mb-8">
                    <div className="flex flex-col sm:flex-row sm:items-center sm:justify-between space-y-4 sm:space-y-0">
                        <div className="flex-1 max-w-md">
                            <div className="relative">
                                <div className="absolute inset-y-0 left-0 pl-3 flex items-center pointer-events-none">
                                    <i className="fas fa-search text-gray-400"></i>
                                </div>
                                <input
                                    type="text"
                                    value={searchTerm}
                                    onChange={(e) => setSearchTerm(e.target.value)}
                                    className="block w-full pl-10 pr-3 py-2 border border-gray-300 rounded-md leading-5 bg-white placeholder-gray-500 focus:outline-none focus:placeholder-gray-400 focus:ring-1 focus:ring-blue-500 focus:border-blue-500"
                                    placeholder="브랜드 검색..."
                                />
                            </div>
                        </div>
                        <div className="text-sm text-gray-500">
                            총 {filteredBrands.length}개의 브랜드
                        </div>
                    </div>
                </div>

                {/* 브랜드 목록 */}
                {loading ? (
                    <div className="flex justify-center items-center py-12">
                        <div className="animate-spin rounded-full h-8 w-8 border-b-2 border-blue-600"></div>
                        <span className="ml-2 text-gray-600">로딩 중...</span>
                    </div>
                ) : filteredBrands.length === 0 ? (
                    <div className="text-center py-12">
                        <i className="fas fa-tags text-gray-400 text-4xl mb-4"></i>
                        <h3 className="text-lg font-medium text-gray-900 mb-2">
                            {searchTerm ? '검색 결과가 없습니다' : '등록된 브랜드가 없습니다'}
                        </h3>
                        <p className="text-gray-500 mb-4">
                            {searchTerm ? '다른 검색어를 시도해보세요' : '첫 번째 브랜드를 추가해보세요'}
                        </p>
                        {!searchTerm && (
                            <button
                                onClick={handleAddBrand}
                                className="bg-blue-600 text-white px-4 py-2 rounded-md hover:bg-blue-700"
                            >
                                브랜드 추가
                            </button>
                        )}
                    </div>
                ) : (
                    <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 xl:grid-cols-4 gap-6">
                        {filteredBrands.map(brand => (
                            <BrandCard
                                key={brand.id}
                                brand={brand}
                                onEdit={handleEditBrand}
                                onDelete={handleDeleteBrand}
                                onManageProducts={onManageProducts}
                            />
                        ))}
                    </div>
                )}
            </div>

            {/* 브랜드 추가/수정 모달 */}
            <BrandModal
                isOpen={modalOpen}
                onClose={handleCloseModal}
                onSave={handleSaveBrand}
                brand={selectedBrand}
                mode={modalMode}
            />
        </div>
    );
}; 
