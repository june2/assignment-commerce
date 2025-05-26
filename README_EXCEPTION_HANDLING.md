# 예외 처리 가이드

## 📋 개요
도메인별 구체적 예외 클래스를 통한 명확하고 일관된 오류 처리

## 🏗️ 예외 계층 구조

```
DomainException (추상)
├── EntityNotFoundException
│   ├── BrandNotFoundException
│   └── ProductNotFoundException
└── InvalidValueException
```

## 🎯 예외 클래스별 용도

| 예외 클래스 | 용도 | HTTP 상태 | 메시지 형식 |
|------------|------|-----------|------------|
| `BrandNotFoundException` | 브랜드 조회 실패 | 404 | "브랜드(id=1)를 찾을 수 없습니다" |
| `ProductNotFoundException` | 상품 조회 실패 | 404 | "상품(id=1)를 찾을 수 없습니다" |
| `InvalidValueException` | 잘못된 값 입력 | 400 | "가격은 음수일 수 없습니다" |
| `DomainException` | 기타 도메인 규칙 위반 | 400 | 사용자 정의 메시지 |


## 📡 HTTP 응답

### 성공
```json
{ "success": true, "data": {...}, "message": null }
```

### 실패  
```json
{ "success": false, "data": null, "message": "브랜드(id=1)를 찾을 수 없습니다" }
```
