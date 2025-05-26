const { useState, useEffect } = React;

// 카테고리별 최저가격 조회 컴포넌트
const CategoryLowestPrice = () => {
    const [data, setData] = useState(null);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    // 데이터 로드
    const loadData = async () => {
        try {
            setLoading(true);
            setError(null);
            const result = await categoryAPI.getLowestByCategory();
            setData(result);
        } catch (err) {
            setError(err.message);
            console.error('카테고리별 최저가격 조회 오류:', err);
        } finally {
            setLoading(false);
        }
    };

    useEffect(() => {
        loadData();
    }, []);

    // 로딩 상태
    if (loading) {
        return (
            <div className="min-h-screen bg-gray-50 flex items-center justify-center">
                <div className="text-center">
                    <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-blue-600 mx-auto mb-4"></div>
                    <p className="text-gray-600">데이터를 불러오는 중...</p>
                </div>
            </div>
        );
    }

    // 에러 상태
    if (error) {
        return (
            <div className="min-h-screen bg-gray-50 flex items-center justify-center">
                <div className="bg-white p-8 rounded-lg shadow-md max-w-md w-full mx-4">
                    <div className="text-center">
                        <i className="fas fa-exclamation-triangle text-red-500 text-4xl mb-4"></i>
                        <h2 className="text-xl font-bold text-gray-800 mb-2">오류 발생</h2>
                        <p className="text-gray-600 mb-4">{error}</p>
                        <button
                            onClick={loadData}
                            className="bg-blue-600 text-white px-4 py-2 rounded hover:bg-blue-700 transition-colors"
                        >
                            다시 시도
                        </button>
                    </div>
                </div>
            </div>
        );
    }

    // 데이터가 없는 경우
    if (!data) {
        return (
            <div className="min-h-screen bg-gray-50 flex items-center justify-center">
                <div className="text-center">
                    <i className="fas fa-inbox text-gray-400 text-4xl mb-4"></i>
                    <p className="text-gray-600">데이터가 없습니다.</p>
                </div>
            </div>
        );
    }

    // 가격 포맷팅 함수
    const formatPrice = (price) => {
        return new Intl.NumberFormat('ko-KR').format(price);
    };

    return (
        <div className="min-h-screen bg-gray-50 py-8">
            <div className="max-w-4xl mx-auto px-4">
                {/* 헤더 */}
                <div className="bg-white rounded-lg shadow-md p-6 mb-6">
                    <div className="flex items-center justify-between">
                        <div>
                            <h1 className="text-2xl font-bold text-gray-800 mb-2">
                                <i className="fas fa-chart-line text-blue-600 mr-3"></i>
                                카테고리별 최저가격 브랜드
                            </h1>
                            <p className="text-gray-600">각 카테고리별 최저가격 브랜드와 상품 가격을 확인하세요</p>
                        </div>
                        <button
                            onClick={loadData}
                            className="bg-blue-600 text-white px-4 py-2 rounded-lg hover:bg-blue-700 transition-colors flex items-center"
                        >
                            <i className="fas fa-sync-alt mr-2"></i>
                            새로고침
                        </button>
                    </div>
                </div>

                {/* 카테고리별 최저가격 테이블 */}
                <div className="bg-white rounded-lg shadow-md overflow-hidden">
                    <div className="px-6 py-4 bg-gray-50 border-b">
                        <h2 className="text-lg font-semibold text-gray-800">카테고리별 최저가격 상품</h2>
                    </div>
                    
                    <div className="overflow-x-auto">
                        <table className="w-full">
                            <thead className="bg-gray-100">
                                <tr>
                                    <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                                        카테고리
                                    </th>
                                    <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                                        브랜드
                                    </th>
                                    <th className="px-6 py-3 text-right text-xs font-medium text-gray-500 uppercase tracking-wider">
                                        가격
                                    </th>
                                </tr>
                            </thead>
                            <tbody className="bg-white divide-y divide-gray-200">
                                {data.items && data.items.map((item, index) => (
                                    <tr key={index} className="hover:bg-gray-50 transition-colors">
                                        <td className="px-6 py-4 whitespace-nowrap">
                                            <div className="flex items-center">
                                                <div className="flex-shrink-0 h-8 w-8 bg-blue-100 rounded-full flex items-center justify-center">
                                                    <i className="fas fa-tag text-blue-600 text-sm"></i>
                                                </div>
                                                <div className="ml-3">
                                                    <div className="text-sm font-medium text-gray-900">
                                                        {item.category}
                                                    </div>
                                                </div>
                                            </div>
                                        </td>
                                        <td className="px-6 py-4 whitespace-nowrap">
                                            <div className="flex items-center">
                                                <div className="flex-shrink-0 h-8 w-8 bg-green-100 rounded-full flex items-center justify-center">
                                                    <span className="text-green-600 text-sm font-bold">
                                                        {item.brandName.charAt(0)}
                                                    </span>
                                                </div>
                                                <div className="ml-3">
                                                    <div className="text-sm font-medium text-gray-900">
                                                        {item.brandName}
                                                    </div>
                                                </div>
                                            </div>
                                        </td>
                                        <td className="px-6 py-4 whitespace-nowrap text-right">
                                            <div className="text-sm font-bold text-gray-900">
                                                {formatPrice(item.price)}원
                                            </div>
                                        </td>
                                    </tr>
                                ))}
                            </tbody>
                        </table>
                    </div>
                </div>

                {/* 총액 표시 */}
                <div className="mt-6 bg-gradient-to-r from-blue-600 to-blue-700 rounded-lg shadow-md p-6 text-white">
                    <div className="flex items-center justify-between">
                        <div>
                            <h3 className="text-lg font-semibold mb-1">총 구매 금액</h3>
                            <p className="text-blue-100 text-sm">모든 카테고리 최저가격 상품 구매 시</p>
                        </div>
                        <div className="text-right">
                            <div className="text-3xl font-bold">
                                {data.total ? formatPrice(data.total) : '0'}원
                            </div>
                            <div className="text-blue-100 text-sm mt-1">
                                <i className="fas fa-calculator mr-1"></i>
                                계산된 총액
                            </div>
                        </div>
                    </div>
                </div>

                {/* 통계 카드들 */}
                <div className="grid grid-cols-1 md:grid-cols-3 gap-6 mt-6">
                    <div className="bg-white rounded-lg shadow-md p-6">
                        <div className="flex items-center">
                            <div className="flex-shrink-0">
                                <div className="h-12 w-12 bg-purple-100 rounded-lg flex items-center justify-center">
                                    <i className="fas fa-list text-purple-600 text-xl"></i>
                                </div>
                            </div>
                            <div className="ml-4">
                                <div className="text-sm font-medium text-gray-500">총 카테고리</div>
                                <div className="text-2xl font-bold text-gray-900">
                                    {data.items ? data.items.length : 0}개
                                </div>
                            </div>
                        </div>
                    </div>

                    <div className="bg-white rounded-lg shadow-md p-6">
                        <div className="flex items-center">
                            <div className="flex-shrink-0">
                                <div className="h-12 w-12 bg-green-100 rounded-lg flex items-center justify-center">
                                    <i className="fas fa-won-sign text-green-600 text-xl"></i>
                                </div>
                            </div>
                            <div className="ml-4">
                                <div className="text-sm font-medium text-gray-500">평균 가격</div>
                                <div className="text-2xl font-bold text-gray-900">
                                    {data.items && data.items.length > 0 
                                        ? formatPrice(Math.round(data.total / data.items.length))
                                        : '0'}원
                                </div>
                            </div>
                        </div>
                    </div>

                    <div className="bg-white rounded-lg shadow-md p-6">
                        <div className="flex items-center">
                            <div className="flex-shrink-0">
                                <div className="h-12 w-12 bg-orange-100 rounded-lg flex items-center justify-center">
                                    <i className="fas fa-trophy text-orange-600 text-xl"></i>
                                </div>
                            </div>
                            <div className="ml-4">
                                <div className="text-sm font-medium text-gray-500">최저 가격</div>
                                <div className="text-2xl font-bold text-gray-900">
                                    {data.items && data.items.length > 0 
                                        ? formatPrice(Math.min(...data.items.map(item => item.price)))
                                        : '0'}원
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    );
};

// 단일 브랜드 최저가격 조회 컴포넌트
const BrandLowestPrice = () => {
    const [data, setData] = useState(null);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    // 데이터 로드
    const loadData = async () => {
        try {
            setLoading(true);
            setError(null);
            const result = await categoryAPI.getLowestByBrand();
            setData(result);
        } catch (err) {
            setError(err.message);
            console.error('단일 브랜드 최저가격 조회 오류:', err);
        } finally {
            setLoading(false);
        }
    };

    useEffect(() => {
        loadData();
    }, []);

    // 로딩 상태
    if (loading) {
        return (
            <div className="min-h-screen bg-gray-50 flex items-center justify-center">
                <div className="text-center">
                    <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-blue-600 mx-auto mb-4"></div>
                    <p className="text-gray-600">데이터를 불러오는 중...</p>
                </div>
            </div>
        );
    }

    // 에러 상태
    if (error) {
        return (
            <div className="min-h-screen bg-gray-50 flex items-center justify-center">
                <div className="bg-white p-8 rounded-lg shadow-md max-w-md w-full mx-4">
                    <div className="text-center">
                        <i className="fas fa-exclamation-triangle text-red-500 text-4xl mb-4"></i>
                        <h2 className="text-xl font-bold text-gray-800 mb-2">오류 발생</h2>
                        <p className="text-gray-600 mb-4">{error}</p>
                        <button
                            onClick={loadData}
                            className="bg-blue-600 text-white px-4 py-2 rounded hover:bg-blue-700 transition-colors"
                        >
                            다시 시도
                        </button>
                    </div>
                </div>
            </div>
        );
    }

    // 데이터가 없는 경우
    if (!data || !data.최저가) {
        return (
            <div className="min-h-screen bg-gray-50 flex items-center justify-center">
                <div className="text-center">
                    <i className="fas fa-inbox text-gray-400 text-4xl mb-4"></i>
                    <p className="text-gray-600">데이터가 없습니다.</p>
                </div>
            </div>
        );
    }

    // 가격 포맷팅 함수 (이미 포맷된 문자열인 경우 그대로 사용)
    const formatPrice = (price) => {
        if (typeof price === 'string') {
            return price; // 이미 포맷된 문자열
        }
        return new Intl.NumberFormat('ko-KR').format(price);
    };

    // 총액에서 숫자만 추출하여 계산에 사용
    const getTotalAmount = () => {
        if (typeof data.총액 === 'string') {
            return parseInt(data.총액.replace(/,/g, ''));
        }
        return data.총액;
    };

    return (
        <div className="min-h-screen bg-gray-50 py-8">
            <div className="max-w-4xl mx-auto px-4">
                {/* 헤더 */}
                <div className="bg-white rounded-lg shadow-md p-6 mb-6">
                    <div className="flex items-center justify-between">
                        <div>
                            <h1 className="text-2xl font-bold text-gray-800 mb-2">
                                <i className="fas fa-store text-green-600 mr-3"></i>
                                단일 브랜드 최저가격
                            </h1>
                            <p className="text-gray-600">한 브랜드로 모든 카테고리 상품을 구매할 때 최저가격 브랜드를 확인하세요</p>
                        </div>
                        <button
                            onClick={loadData}
                            className="bg-green-600 text-white px-4 py-2 rounded-lg hover:bg-green-700 transition-colors flex items-center"
                        >
                            <i className="fas fa-sync-alt mr-2"></i>
                            새로고침
                        </button>
                    </div>
                </div>

                {/* 브랜드 정보 */}
                <div className="bg-gradient-to-r from-green-600 to-green-700 rounded-lg shadow-md p-6 text-white mb-6">
                    <div className="flex items-center justify-between">
                        <div>
                            <h3 className="text-lg font-semibold mb-1">최저가격 브랜드</h3>
                            <p className="text-green-100 text-sm">모든 카테고리 상품을 한 브랜드에서 구매 시</p>
                        </div>
                        <div className="text-right">
                            <div className="text-3xl font-bold">
                                {data.최저가.브랜드}
                            </div>
                            <div className="text-green-100 text-sm mt-1">
                                <i className="fas fa-crown mr-1"></i>
                                최적 브랜드
                            </div>
                        </div>
                    </div>
                </div>

                {/* 카테고리별 상품 가격 테이블 */}
                <div className="bg-white rounded-lg shadow-md overflow-hidden mb-6">
                    <div className="px-6 py-4 bg-gray-50 border-b">
                        <h2 className="text-lg font-semibold text-gray-800">카테고리별 상품 가격</h2>
                    </div>
                    
                    <div className="overflow-x-auto">
                        <table className="w-full">
                            <thead className="bg-gray-100">
                                <tr>
                                    <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                                        카테고리
                                    </th>
                                    <th className="px-6 py-3 text-right text-xs font-medium text-gray-500 uppercase tracking-wider">
                                        가격
                                    </th>
                                </tr>
                            </thead>
                            <tbody className="bg-white divide-y divide-gray-200">
                                {data.최저가.카테고리 && data.최저가.카테고리.map((item, index) => (
                                    <tr key={index} className="hover:bg-gray-50 transition-colors">
                                        <td className="px-6 py-4 whitespace-nowrap">
                                            <div className="flex items-center">
                                                <div className="flex-shrink-0 h-8 w-8 bg-green-100 rounded-full flex items-center justify-center">
                                                    <i className="fas fa-tag text-green-600 text-sm"></i>
                                                </div>
                                                <div className="ml-3">
                                                    <div className="text-sm font-medium text-gray-900">
                                                        {item.카테고리}
                                                    </div>
                                                </div>
                                            </div>
                                        </td>
                                        <td className="px-6 py-4 whitespace-nowrap text-right">
                                            <div className="text-sm font-bold text-gray-900">
                                                {item.가격}원
                                            </div>
                                        </td>
                                    </tr>
                                ))}
                            </tbody>
                        </table>
                    </div>
                </div>

                {/* 총액 표시 */}
                <div className="bg-gradient-to-r from-green-600 to-green-700 rounded-lg shadow-md p-6 text-white mb-6">
                    <div className="flex items-center justify-between">
                        <div>
                            <h3 className="text-lg font-semibold mb-1">총 구매 금액</h3>
                            <p className="text-green-100 text-sm">{data.최저가.브랜드} 브랜드에서 모든 카테고리 상품 구매 시</p>
                        </div>
                        <div className="text-right">
                            <div className="text-3xl font-bold">
                                {data.총액}원
                            </div>
                            <div className="text-green-100 text-sm mt-1">
                                <i className="fas fa-calculator mr-1"></i>
                                최저 총액
                            </div>
                        </div>
                    </div>
                </div>

                {/* 통계 카드들 */}
                <div className="grid grid-cols-1 md:grid-cols-3 gap-6">
                    <div className="bg-white rounded-lg shadow-md p-6">
                        <div className="flex items-center">
                            <div className="flex-shrink-0">
                                <div className="h-12 w-12 bg-blue-100 rounded-lg flex items-center justify-center">
                                    <i className="fas fa-list text-blue-600 text-xl"></i>
                                </div>
                            </div>
                            <div className="ml-4">
                                <div className="text-sm font-medium text-gray-500">총 카테고리</div>
                                <div className="text-2xl font-bold text-gray-900">
                                    {data.최저가.카테고리 ? data.최저가.카테고리.length : 0}개
                                </div>
                            </div>
                        </div>
                    </div>

                    <div className="bg-white rounded-lg shadow-md p-6">
                        <div className="flex items-center">
                            <div className="flex-shrink-0">
                                <div className="h-12 w-12 bg-green-100 rounded-lg flex items-center justify-center">
                                    <i className="fas fa-won-sign text-green-600 text-xl"></i>
                                </div>
                            </div>
                            <div className="ml-4">
                                <div className="text-sm font-medium text-gray-500">평균 가격</div>
                                <div className="text-2xl font-bold text-gray-900">
                                    {data.최저가.카테고리 && data.최저가.카테고리.length > 0 
                                        ? formatPrice(Math.round(getTotalAmount() / data.최저가.카테고리.length))
                                        : '0'}원
                                </div>
                            </div>
                        </div>
                    </div>

                    <div className="bg-white rounded-lg shadow-md p-6">
                        <div className="flex items-center">
                            <div className="flex-shrink-0">
                                <div className="h-12 w-12 bg-purple-100 rounded-lg flex items-center justify-center">
                                    <i className="fas fa-store text-purple-600 text-xl"></i>
                                </div>
                            </div>
                            <div className="ml-4">
                                <div className="text-sm font-medium text-gray-500">브랜드</div>
                                <div className="text-2xl font-bold text-gray-900">
                                    {data.최저가.브랜드}
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    );
};

// 카테고리별 가격 범위 조회 컴포넌트
const CategoryPriceRange = () => {
    const [data, setData] = useState(null);
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState(null);
    const [selectedCategory, setSelectedCategory] = useState('');
    const [categories] = useState(['상의', '아우터', '바지', '스니커즈', '가방', '모자', '양말', '액세서리']);

    // 데이터 로드
    const loadData = async (category) => {
        if (!category) return;
        
        try {
            setLoading(true);
            setError(null);
            const result = await categoryAPI.getCategoryPriceRange(category);
            setData(result);
        } catch (err) {
            setError(err.message);
            console.error('카테고리별 가격 범위 조회 오류:', err);
        } finally {
            setLoading(false);
        }
    };

    // 카테고리 선택 핸들러
    const handleCategorySelect = (category) => {
        setSelectedCategory(category);
        loadData(category);
    };

    return (
        <div className="min-h-screen bg-gray-50 py-8">
            <div className="max-w-4xl mx-auto px-4">
                {/* 헤더 */}
                <div className="bg-white rounded-lg shadow-md p-6 mb-6">
                    <div className="flex items-center justify-between">
                        <div>
                            <h1 className="text-2xl font-bold text-gray-800 mb-2">
                                <i className="fas fa-search text-purple-600 mr-3"></i>
                                카테고리별 가격 범위
                            </h1>
                            <p className="text-gray-600">특정 카테고리의 최저가와 최고가 브랜드를 확인하세요</p>
                        </div>
                    </div>
                </div>

                {/* 카테고리 선택 */}
                <div className="bg-white rounded-lg shadow-md p-6 mb-6">
                    <h2 className="text-lg font-semibold text-gray-800 mb-4">카테고리 선택</h2>
                    <div className="grid grid-cols-2 md:grid-cols-4 gap-3">
                        {categories.map((category) => (
                            <button
                                key={category}
                                onClick={() => handleCategorySelect(category)}
                                className={`p-3 rounded-lg border-2 transition-all ${
                                    selectedCategory === category
                                        ? 'border-purple-500 bg-purple-50 text-purple-700'
                                        : 'border-gray-200 hover:border-purple-300 hover:bg-purple-50'
                                }`}
                            >
                                <i className="fas fa-tag mr-2"></i>
                                {category}
                            </button>
                        ))}
                    </div>
                </div>

                {/* 로딩 상태 */}
                {loading && (
                    <div className="bg-white rounded-lg shadow-md p-8">
                        <div className="text-center">
                            <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-purple-600 mx-auto mb-4"></div>
                            <p className="text-gray-600">데이터를 불러오는 중...</p>
                        </div>
                    </div>
                )}

                {/* 에러 상태 */}
                {error && (
                    <div className="bg-white rounded-lg shadow-md p-8">
                        <div className="text-center">
                            <i className="fas fa-exclamation-triangle text-red-500 text-4xl mb-4"></i>
                            <h2 className="text-xl font-bold text-gray-800 mb-2">오류 발생</h2>
                            <p className="text-gray-600 mb-4">{error}</p>
                            <button
                                onClick={() => loadData(selectedCategory)}
                                className="bg-purple-600 text-white px-4 py-2 rounded hover:bg-purple-700 transition-colors"
                            >
                                다시 시도
                            </button>
                        </div>
                    </div>
                )}

                {/* 결과 표시 */}
                {data && !loading && (
                    <>
                        {/* 카테고리 정보 */}
                        <div className="bg-gradient-to-r from-purple-600 to-purple-700 rounded-lg shadow-md p-6 text-white mb-6">
                            <div className="text-center">
                                <h3 className="text-2xl font-bold mb-2">{data.카테고리}</h3>
                                <p className="text-purple-100">카테고리별 최저가 및 최고가 브랜드 정보</p>
                            </div>
                        </div>

                        {/* 최저가 및 최고가 정보 */}
                        <div className="grid grid-cols-1 md:grid-cols-2 gap-6 mb-6">
                            {/* 최저가 */}
                            <div className="bg-white rounded-lg shadow-md overflow-hidden">
                                <div className="px-6 py-4 bg-green-50 border-b">
                                    <h3 className="text-lg font-semibold text-green-800 flex items-center">
                                        <i className="fas fa-arrow-down mr-2"></i>
                                        최저가
                                    </h3>
                                </div>
                                <div className="p-6">
                                    {data.최저가 && data.최저가.map((item, index) => (
                                        <div key={index} className="flex items-center justify-between p-4 bg-green-50 rounded-lg mb-3 last:mb-0">
                                            <div className="flex items-center">
                                                <div className="flex-shrink-0 h-10 w-10 bg-green-100 rounded-full flex items-center justify-center">
                                                    <span className="text-green-600 font-bold">
                                                        {item.브랜드.charAt(0)}
                                                    </span>
                                                </div>
                                                <div className="ml-3">
                                                    <div className="text-sm font-medium text-gray-900">
                                                        {item.브랜드}
                                                    </div>
                                                </div>
                                            </div>
                                            <div className="text-lg font-bold text-green-600">
                                                {item.가격}원
                                            </div>
                                        </div>
                                    ))}
                                </div>
                            </div>

                            {/* 최고가 */}
                            <div className="bg-white rounded-lg shadow-md overflow-hidden">
                                <div className="px-6 py-4 bg-red-50 border-b">
                                    <h3 className="text-lg font-semibold text-red-800 flex items-center">
                                        <i className="fas fa-arrow-up mr-2"></i>
                                        최고가
                                    </h3>
                                </div>
                                <div className="p-6">
                                    {data.최고가 && data.최고가.map((item, index) => (
                                        <div key={index} className="flex items-center justify-between p-4 bg-red-50 rounded-lg mb-3 last:mb-0">
                                            <div className="flex items-center">
                                                <div className="flex-shrink-0 h-10 w-10 bg-red-100 rounded-full flex items-center justify-center">
                                                    <span className="text-red-600 font-bold">
                                                        {item.브랜드.charAt(0)}
                                                    </span>
                                                </div>
                                                <div className="ml-3">
                                                    <div className="text-sm font-medium text-gray-900">
                                                        {item.브랜드}
                                                    </div>
                                                </div>
                                            </div>
                                            <div className="text-lg font-bold text-red-600">
                                                {item.가격}원
                                            </div>
                                        </div>
                                    ))}
                                </div>
                            </div>
                        </div>

                        {/* 가격 차이 통계 */}
                        {data.최저가 && data.최고가 && data.최저가.length > 0 && data.최고가.length > 0 && (
                            <div className="bg-white rounded-lg shadow-md p-6">
                                <h3 className="text-lg font-semibold text-gray-800 mb-4">가격 차이 분석</h3>
                                <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
                                    <div className="text-center p-4 bg-green-50 rounded-lg">
                                        <div className="text-2xl font-bold text-green-600">
                                            {data.최저가[0].가격}원
                                        </div>
                                        <div className="text-sm text-gray-600 mt-1">최저가</div>
                                    </div>
                                    <div className="text-center p-4 bg-red-50 rounded-lg">
                                        <div className="text-2xl font-bold text-red-600">
                                            {data.최고가[0].가격}원
                                        </div>
                                        <div className="text-sm text-gray-600 mt-1">최고가</div>
                                    </div>
                                    <div className="text-center p-4 bg-purple-50 rounded-lg">
                                        <div className="text-2xl font-bold text-purple-600">
                                            {(() => {
                                                const lowest = parseInt(data.최저가[0].가격.replace(/,/g, ''));
                                                const highest = parseInt(data.최고가[0].가격.replace(/,/g, ''));
                                                return new Intl.NumberFormat('ko-KR').format(highest - lowest);
                                            })()}원
                                        </div>
                                        <div className="text-sm text-gray-600 mt-1">가격 차이</div>
                                    </div>
                                </div>
                            </div>
                        )}
                    </>
                )}

                {/* 초기 상태 */}
                {!selectedCategory && !loading && (
                    <div className="bg-white rounded-lg shadow-md p-8">
                        <div className="text-center">
                            <i className="fas fa-search text-gray-400 text-4xl mb-4"></i>
                            <h3 className="text-xl font-bold text-gray-800 mb-2">카테고리를 선택하세요</h3>
                            <p className="text-gray-600">위에서 카테고리를 선택하면 해당 카테고리의 최저가와 최고가 브랜드 정보를 확인할 수 있습니다.</p>
                        </div>
                    </div>
                )}
            </div>
        </div>
    );
}; 