const { useState } = React;

// 메인 애플리케이션 컴포넌트
const App = () => {
    const [currentView, setCurrentView] = useState('brands'); // 'brands', 'products', 'category-lowest', 'brand-lowest', 'category-range'
    const [selectedBrandForProducts, setSelectedBrandForProducts] = useState(null);

    // 상품 관리 화면으로 이동
    const handleManageProducts = (brand) => {
        setSelectedBrandForProducts(brand);
        setCurrentView('products');
    };

    // 브랜드 목록으로 돌아가기
    const handleBackToBrands = () => {
        setCurrentView('brands');
        setSelectedBrandForProducts(null);
    };

    // 카테고리별 최저가격 조회 화면으로 이동
    const handleViewCategoryLowest = () => {
        setCurrentView('category-lowest');
        setSelectedBrandForProducts(null);
    };

    // 단일 브랜드 최저가격 조회 화면으로 이동
    const handleViewBrandLowest = () => {
        setCurrentView('brand-lowest');
        setSelectedBrandForProducts(null);
    };

    // 카테고리별 가격 범위 조회 화면으로 이동
    const handleViewCategoryRange = () => {
        setCurrentView('category-range');
        setSelectedBrandForProducts(null);
    };

    // 네비게이션 컴포넌트
    const Navigation = () => (
        <div className="bg-white shadow-sm border-b mb-6">
            <div className="max-w-7xl mx-auto px-4">
                <div className="flex space-x-8 overflow-x-auto">
                    <button
                        onClick={() => setCurrentView('brands')}
                        className={`py-4 px-2 border-b-2 font-medium text-sm whitespace-nowrap ${
                            currentView === 'brands'
                                ? 'border-blue-500 text-blue-600'
                                : 'border-transparent text-gray-500 hover:text-gray-700 hover:border-gray-300'
                        }`}
                    >
                        <i className="fas fa-store mr-2"></i>
                        브랜드 관리
                    </button>
                    <button
                        onClick={handleViewCategoryLowest}
                        className={`py-4 px-2 border-b-2 font-medium text-sm whitespace-nowrap ${
                            currentView === 'category-lowest'
                                ? 'border-blue-500 text-blue-600'
                                : 'border-transparent text-gray-500 hover:text-gray-700 hover:border-gray-300'
                        }`}
                    >
                        <i className="fas fa-chart-line mr-2"></i>
                        카테고리별 최저가격
                    </button>
                    <button
                        onClick={handleViewBrandLowest}
                        className={`py-4 px-2 border-b-2 font-medium text-sm whitespace-nowrap ${
                            currentView === 'brand-lowest'
                                ? 'border-green-500 text-green-600'
                                : 'border-transparent text-gray-500 hover:text-gray-700 hover:border-gray-300'
                        }`}
                    >
                        <i className="fas fa-crown mr-2"></i>
                        단일 브랜드 최저가격
                    </button>
                    <button
                        onClick={handleViewCategoryRange}
                        className={`py-4 px-2 border-b-2 font-medium text-sm whitespace-nowrap ${
                            currentView === 'category-range'
                                ? 'border-purple-500 text-purple-600'
                                : 'border-transparent text-gray-500 hover:text-gray-700 hover:border-gray-300'
                        }`}
                    >
                        <i className="fas fa-search mr-2"></i>
                        카테고리별 가격 범위
                    </button>
                </div>
            </div>
        </div>
    );

    // 카테고리별 가격 범위 조회 화면
    if (currentView === 'category-range') {
        return (
            <div>
                <Navigation />
                <CategoryPriceRange />
            </div>
        );
    }

    // 단일 브랜드 최저가격 조회 화면
    if (currentView === 'brand-lowest') {
        return (
            <div>
                <Navigation />
                <BrandLowestPrice />
            </div>
        );
    }

    // 카테고리별 최저가격 조회 화면
    if (currentView === 'category-lowest') {
        return (
            <div>
                <Navigation />
                <CategoryLowestPrice />
            </div>
        );
    }

    // 상품 관리 화면 렌더링
    if (currentView === 'products' && selectedBrandForProducts) {
        return (
            <div>
                <Navigation />
                <ProductManagement
                    brand={selectedBrandForProducts}
                    onBack={handleBackToBrands}
                />
            </div>
        );
    }

    // 브랜드 목록 화면 렌더링 (기본)
    return (
        <div>
            <Navigation />
            <BrandManagement
                onManageProducts={handleManageProducts}
            />
        </div>
    );
};

// 앱 렌더링
ReactDOM.render(<App />, document.getElementById('root')); 
