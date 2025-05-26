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