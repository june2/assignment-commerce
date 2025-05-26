const { useState } = React;

// 메인 애플리케이션 컴포넌트
const App = () => {
    const [currentView, setCurrentView] = useState('brands'); // 'brands' or 'products'
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

    // 상품 관리 화면 렌더링
    if (currentView === 'products' && selectedBrandForProducts) {
        return (
            <ProductManagement
                brand={selectedBrandForProducts}
                onBack={handleBackToBrands}
            />
        );
    }

    // 브랜드 목록 화면 렌더링 (기본)
    return (
        <BrandManagement
            onManageProducts={handleManageProducts}
        />
    );
};

// 앱 렌더링
ReactDOM.render(<App />, document.getElementById('root')); 
