const { useState, useEffect } = React;

// 상품 생성/수정 모달 컴포넌트
const ProductModal = ({ isOpen, onClose, onSave, product, mode, brandId, brandName }) => {
    const [formData, setFormData] = useState({
        name: '',
        category: '',
        price: ''
    });
    const [loading, setLoading] = useState(false);
    const [categories, setCategories] = useState([]);
    const [categoriesLoading, setCategoriesLoading] = useState(true);

    // 카테고리 목록 로드
    useEffect(() => {
        const loadCategories = async () => {
            try {
                setCategoriesLoading(true);
                const categoryList = await categoryAPI.getCategories();
                setCategories(categoryList);
            } catch (error) {
                console.error('카테고리 목록 로드 실패:', error);
                alert('카테고리 목록을 불러오는데 실패했습니다.');
            } finally {
                setCategoriesLoading(false);
            }
        };

        if (isOpen) {
            loadCategories();
        }
    }, [isOpen]);

    // 수정 모드일 때 기존 데이터로 폼 초기화 (카테고리 로딩 완료 후)
    useEffect(() => {
        if (mode === 'edit' && product && !categoriesLoading && categories.length > 0) {
            // 기존 카테고리 값이 한국어 표시명인 경우 enum 값으로 변환
            let categoryValue = product.category || '';
            
            // 카테고리 목록에서 매칭되는 값 찾기
            const matchingCategory = categories.find(cat => 
                cat.value === categoryValue || cat.label === categoryValue
            );
            
            if (matchingCategory) {
                categoryValue = matchingCategory.value;
            }
            
            setFormData({
                name: product.name || '',
                category: categoryValue,
                price: product.price?.toString() || ''
            });
        } else if (mode === 'create' || !product) {
            setFormData({
                name: '',
                category: '',
                price: ''
            });
        }
    }, [mode, product, isOpen, categoriesLoading, categories]);

    const handleInputChange = (field, value) => {
        setFormData(prev => ({
            ...prev,
            [field]: value
        }));
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        
        if (!formData.name.trim()) {
            alert('상품명을 입력해주세요.');
            return;
        }
        
        if (!formData.category.trim()) {
            alert('카테고리를 선택해주세요.');
            return;
        }
        
        const price = parseInt(formData.price);
        if (!price || price <= 0) {
            alert('올바른 가격을 입력해주세요.');
            return;
        }

        setLoading(true);
        try {
            const productData = {
                name: formData.name.trim(),
                category: formData.category.trim(),
                price: price,
                brandId: brandId
            };

            console.log('상품 저장 시도:', productData);

            if (mode === 'edit') {
                const result = await productAPI.update(product.id, productData);
                console.log('상품 수정 완료:', result);
            } else {
                const result = await productAPI.create(productData);
                console.log('상품 생성 완료:', result);
            }
            
            // 성공 후 콜백 호출
            console.log('onSave 콜백 호출 시작');
            await onSave();
            console.log('onSave 콜백 완료');
            
            onClose();
            
            // 성공 메시지 표시
            alert(mode === 'edit' ? '상품이 수정되었습니다.' : '상품이 추가되었습니다.');
        } catch (error) {
            console.error('상품 저장 실패:', error);
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
                        {mode === 'edit' ? '상품 수정' : '상품 추가'}
                    </h2>
                    <button
                        onClick={onClose}
                        className="text-gray-500 hover:text-gray-700"
                    >
                        <i className="fas fa-times"></i>
                    </button>
                </div>
                <div className="mb-4 p-3 bg-blue-50 rounded-md">
                    <p className="text-sm text-blue-800">
                        <i className="fas fa-tag mr-2"></i>
                        브랜드: {brandName} (ID: {brandId})
                    </p>
                </div>
                <form onSubmit={handleSubmit}>
                    <div className="mb-4">
                        <label className="block text-sm font-medium text-gray-700 mb-2">
                            상품명
                        </label>
                        <input
                            type="text"
                            value={formData.name}
                            onChange={(e) => handleInputChange('name', e.target.value)}
                            className="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
                            placeholder="상품명을 입력하세요"
                            disabled={loading}
                        />
                    </div>
                    <div className="mb-4">
                        <label className="block text-sm font-medium text-gray-700 mb-2">
                            카테고리
                        </label>
                        {categoriesLoading ? (
                            <div className="w-full px-3 py-2 border border-gray-300 rounded-md bg-gray-50 text-gray-500">
                                카테고리 로딩 중...
                            </div>
                        ) : (
                            <select
                                value={formData.category}
                                onChange={(e) => handleInputChange('category', e.target.value)}
                                className="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
                                disabled={loading}
                            >
                                <option value="">카테고리를 선택하세요</option>
                                {categories.map((category) => (
                                    <option key={category.value} value={category.value}>
                                        {category.label}
                                    </option>
                                ))}
                            </select>
                        )}
                    </div>
                    <div className="mb-4">
                        <label className="block text-sm font-medium text-gray-700 mb-2">
                            가격
                        </label>
                        <input
                            type="number"
                            value={formData.price}
                            onChange={(e) => handleInputChange('price', e.target.value)}
                            className="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
                            placeholder="가격을 입력하세요"
                            min="1"
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

// 상품 카드 컴포넌트
const ProductCard = ({ product, onEdit, onDelete }) => {
    const [deleting, setDeleting] = useState(false);

    const handleDelete = async () => {
        if (!confirm(`'${product.name}' 상품을 삭제하시겠습니까?`)) {
            return;
        }

        setDeleting(true);
        try {
            await productAPI.delete(product.id);
            console.log('상품 삭제 완료:', product.id);
            onDelete();
        } catch (error) {
            console.error('상품 삭제 실패:', error);
            alert(error.message);
        } finally {
            setDeleting(false);
        }
    };

    const formatPrice = (price) => {
        return new Intl.NumberFormat('ko-KR').format(price);
    };

    return (
        <div className="bg-white rounded-lg shadow-md p-4 hover:shadow-lg transition-shadow">
            <div className="flex justify-between items-start mb-3">
                <div className="flex-1">
                    <h4 className="text-lg font-semibold text-gray-900 mb-1">
                        {product.name}
                    </h4>
                    <p className="text-sm text-gray-600 mb-1">
                        <i className="fas fa-layer-group mr-1"></i>
                        {product.category}
                    </p>
                    <p className="text-lg font-bold text-blue-600">
                        ₩{formatPrice(product.price)}
                    </p>
                </div>
                <div className="flex space-x-1">
                    <button
                        onClick={() => onEdit(product)}
                        className="p-1.5 text-blue-600 hover:bg-blue-50 rounded-md transition-colors"
                        title="수정"
                    >
                        <i className="fas fa-edit text-sm"></i>
                    </button>
                    <button
                        onClick={handleDelete}
                        disabled={deleting}
                        className="p-1.5 text-red-600 hover:bg-red-50 rounded-md transition-colors disabled:opacity-50"
                        title="삭제"
                    >
                        <i className="fas fa-trash text-sm"></i>
                    </button>
                </div>
            </div>
            <div className="text-xs text-gray-500">
                ID: {product.id} | 브랜드ID: {product.brandId}
            </div>
        </div>
    );
};

// 브랜드별 상품 관리 컴포넌트
const ProductManagement = ({ brand, onBack }) => {
    const [products, setProducts] = useState([]);
    const [loading, setLoading] = useState(true);
    const [modalOpen, setModalOpen] = useState(false);
    const [modalMode, setModalMode] = useState('create');
    const [selectedProduct, setSelectedProduct] = useState(null);
    const [searchTerm, setSearchTerm] = useState('');

    // 상품 목록 로드
    const loadProducts = async () => {
        console.log('상품 목록 로드 시작, 브랜드 ID:', brand.id, '타입:', typeof brand.id);
        setLoading(true);
        try {
            const data = await productAPI.getByBrandId(brand.id);
            console.log('로드된 상품 목록:', data);
            console.log('상품 개수:', data.length);
            setProducts(data);
            console.log('상태 업데이트 완료, products 길이:', data.length);
        } catch (error) {
            console.error('상품 목록 로드 실패:', error);
            alert(error.message);
        } finally {
            setLoading(false);
            console.log('로딩 상태 해제');
        }
    };

    useEffect(() => {
        console.log('ProductManagement useEffect 실행, 브랜드:', brand);
        loadProducts();
    }, [brand.id]);

    // 상품 추가 모달 열기
    const handleAddProduct = () => {
        setModalMode('create');
        setSelectedProduct(null);
        setModalOpen(true);
    };

    // 상품 수정 모달 열기
    const handleEditProduct = (product) => {
        setModalMode('edit');
        setSelectedProduct(product);
        setModalOpen(true);
    };

    // 모달 닫기
    const handleCloseModal = () => {
        setModalOpen(false);
        setSelectedProduct(null);
    };

    // 상품 저장 후 목록 새로고침
    const handleSaveProduct = async () => {
        console.log('상품 저장 완료, 목록 새로고침 시작');
        await loadProducts();
        console.log('목록 새로고침 완료');
    };

    // 상품 삭제 후 목록 새로고침
    const handleDeleteProduct = async () => {
        console.log('상품 삭제 완료, 목록 새로고침 시작');
        await loadProducts();
        console.log('목록 새로고침 완료');
    };

    // 검색 필터링
    const filteredProducts = products.filter(product =>
        product.name.toLowerCase().includes(searchTerm.toLowerCase()) ||
        product.category.toLowerCase().includes(searchTerm.toLowerCase())
    );

    console.log('렌더링 시점 - 전체 상품:', products.length, '필터링된 상품:', filteredProducts.length, '로딩:', loading);

    const totalValue = filteredProducts.reduce((sum, product) => sum + product.price, 0);

    return (
        <div className="min-h-screen bg-gray-50">
            {/* 헤더 */}
            <div className="bg-white shadow-sm">
                <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
                    <div className="flex justify-between items-center py-6">
                        <div className="flex items-center space-x-4">
                            <button
                                onClick={onBack}
                                className="text-gray-600 hover:text-gray-900 flex items-center space-x-2"
                            >
                                <i className="fas fa-arrow-left"></i>
                                <span>브랜드 목록으로</span>
                            </button>
                            <div className="border-l border-gray-300 pl-4">
                                <h1 className="text-3xl font-bold text-gray-900">
                                    {brand.name} 상품 관리
                                </h1>
                                <p className="mt-1 text-sm text-gray-500">
                                    {brand.name} 브랜드의 상품을 관리할 수 있습니다. (브랜드 ID: {brand.id})
                                </p>
                            </div>
                        </div>
                        <button
                            onClick={handleAddProduct}
                            className="bg-blue-600 text-white px-4 py-2 rounded-md hover:bg-blue-700 flex items-center space-x-2"
                        >
                            <i className="fas fa-plus"></i>
                            <span>상품 추가</span>
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
                                    placeholder="상품명 또는 카테고리 검색..."
                                />
                            </div>
                        </div>
                        <div className="text-sm text-gray-500 space-y-1">
                            <div>총 {filteredProducts.length}개의 상품</div>
                            <div>총 가치: ₩{new Intl.NumberFormat('ko-KR').format(totalValue)}</div>
                        </div>
                    </div>
                </div>

                {/* 상품 목록 */}
                {loading ? (
                    <div className="flex justify-center items-center py-12">
                        <div className="animate-spin rounded-full h-8 w-8 border-b-2 border-blue-600"></div>
                        <span className="ml-2 text-gray-600">로딩 중...</span>
                    </div>
                ) : filteredProducts.length === 0 ? (
                    <div className="text-center py-12">
                        <i className="fas fa-box-open text-gray-400 text-4xl mb-4"></i>
                        <h3 className="text-lg font-medium text-gray-900 mb-2">
                            {searchTerm ? '검색 결과가 없습니다' : '등록된 상품이 없습니다'}
                        </h3>
                        <p className="text-gray-500 mb-4">
                            {searchTerm ? '다른 검색어를 시도해보세요' : `${brand.name} 브랜드의 첫 번째 상품을 추가해보세요`}
                        </p>
                        <div className="text-xs text-gray-400 mb-4">
                            디버그: 전체 상품 {products.length}개, 브랜드 ID: {brand.id}
                        </div>
                        {!searchTerm && (
                            <button
                                onClick={handleAddProduct}
                                className="bg-blue-600 text-white px-4 py-2 rounded-md hover:bg-blue-700"
                            >
                                상품 추가
                            </button>
                        )}
                    </div>
                ) : (
                    <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 xl:grid-cols-4 gap-4">
                        {filteredProducts.map(product => (
                            <ProductCard
                                key={product.id}
                                product={product}
                                onEdit={handleEditProduct}
                                onDelete={handleDeleteProduct}
                            />
                        ))}
                    </div>
                )}
            </div>

            {/* 상품 추가/수정 모달 */}
            <ProductModal
                isOpen={modalOpen}
                onClose={handleCloseModal}
                onSave={handleSaveProduct}
                product={selectedProduct}
                mode={modalMode}
                brandId={brand.id}
                brandName={brand.name}
            />
        </div>
    );
}; 
