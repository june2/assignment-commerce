// API 호출 함수들
const brandAPI = {
    // 모든 브랜드 조회
    getAll: async () => {
        const response = await fetch('/api/v1/brands');
        if (!response.ok) throw new Error('브랜드 목록 조회 실패');
        return response.json();
    },

    // 브랜드 생성
    create: async (name) => {
        const response = await fetch('/api/v1/brands', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({ name }),
        });
        if (!response.ok) throw new Error('브랜드 생성 실패');
        return response.json();
    },

    // 브랜드 수정
    update: async (id, name) => {
        const response = await fetch(`/api/v1/brands/${id}`, {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({ name }),
        });
        if (!response.ok) throw new Error('브랜드 수정 실패');
        return response.json();
    },

    // 브랜드 삭제
    delete: async (id) => {
        const response = await fetch(`/api/v1/brands/${id}`, {
            method: 'DELETE',
        });
        if (!response.ok) throw new Error('브랜드 삭제 실패');
    },

    // 브랜드 단건 조회
    getById: async (id) => {
        const response = await fetch(`/api/v1/brands/${id}`);
        if (!response.ok) throw new Error('브랜드 조회 실패');
        return response.json();
    }
};

// 상품 API 호출 함수들
const productAPI = {
    // 모든 상품 조회
    getAll: async () => {
        const response = await fetch('/api/v1/products');
        if (!response.ok) throw new Error('상품 목록 조회 실패');
        const products = await response.json();
        console.log('전체 상품 목록 API 응답:', products);
        return products;
    },

    // 브랜드별 상품 조회 (클라이언트 사이드 필터링)
    getByBrandId: async (brandId) => {
        console.log('브랜드별 상품 조회 요청, 브랜드 ID:', brandId, '타입:', typeof brandId);
        const allProducts = await productAPI.getAll();
        
        // 타입 안전성을 위해 숫자로 변환하여 비교
        const targetBrandId = Number(brandId);
        const filteredProducts = allProducts.filter(product => {
            const productBrandId = Number(product.brandId);
            console.log(`상품 ${product.id}: brandId=${product.brandId}(${typeof product.brandId}) vs 타겟=${targetBrandId}(${typeof targetBrandId}) => 매치: ${productBrandId === targetBrandId}`);
            return productBrandId === targetBrandId;
        });
        
        console.log('필터링된 상품 목록:', filteredProducts);
        return filteredProducts;
    },

    // 상품 생성
    create: async (productData) => {
        console.log('상품 생성 API 요청:', productData);
        const response = await fetch('/api/v1/products', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(productData),
        });
        if (!response.ok) {
            const errorText = await response.text();
            console.error('상품 생성 API 오류:', response.status, errorText);
            throw new Error('상품 생성 실패');
        }
        const result = await response.json();
        console.log('상품 생성 API 응답:', result);
        return result;
    },

    // 상품 수정
    update: async (id, productData) => {
        console.log('상품 수정 API 요청:', id, productData);
        const response = await fetch(`/api/v1/products/${id}`, {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(productData),
        });
        if (!response.ok) {
            const errorText = await response.text();
            console.error('상품 수정 API 오류:', response.status, errorText);
            throw new Error('상품 수정 실패');
        }
        const result = await response.json();
        console.log('상품 수정 API 응답:', result);
        return result;
    },

    // 상품 삭제
    delete: async (id) => {
        console.log('상품 삭제 API 요청:', id);
        const response = await fetch(`/api/v1/products/${id}`, {
            method: 'DELETE',
        });
        if (!response.ok) {
            const errorText = await response.text();
            console.error('상품 삭제 API 오류:', response.status, errorText);
            throw new Error('상품 삭제 실패');
        }
        console.log('상품 삭제 API 완료');
    },

    // 상품 단건 조회
    getById: async (id) => {
        const response = await fetch(`/api/v1/products/${id}`);
        if (!response.ok) throw new Error('상품 조회 실패');
        return response.json();
    }
};

// 카테고리별 최저가격 조회 API
const categoryAPI = {
    // 카테고리 목록 조회
    getCategories: async () => {
        const response = await fetch('/api/v1/prices/categories');
        if (!response.ok) throw new Error('카테고리 목록 조회 실패');
        return response.json();
    },

    // 카테고리별 최저가격 브랜드와 상품 가격, 총액 조회
    getLowestByCategory: async () => {
        const response = await fetch('/api/v1/prices/lowest-by-category');
        if (!response.ok) throw new Error('카테고리별 최저가격 조회 실패');
        return response.json();
    },

    // 단일 브랜드로 모든 카테고리 상품 구매 시 최저가격 브랜드 조회
    getLowestByBrand: async () => {
        const response = await fetch('/api/v1/prices/lowest-by-brand');
        if (!response.ok) throw new Error('단일 브랜드 최저가격 조회 실패');
        return response.json();
    },

    // 카테고리별 최저, 최고 가격 브랜드와 상품 가격 조회
    getCategoryPriceRange: async (category) => {
        const response = await fetch(`/api/v1/prices/category/${encodeURIComponent(category)}/price-range`);
        if (!response.ok) throw new Error('카테고리별 가격 범위 조회 실패');
        return response.json();
    }
}; 
