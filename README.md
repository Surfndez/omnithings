# OmniThings

## 服務介紹
OmniThings 提供了物聯網開發者，在最短的過程中建立起以 JSON Object 為溝通基礎，並專屬於開發者的高效 API 服務的管理介面。 

## 開發流程
### 取得開發代碼與憑證
進入憑證管理頁面，能夠取得專屬的開發代碼(SID)與憑證(Program Key)，開發者透過代號與 ASUS Cloud API 進行溝通，並透過 Program Key 加密組成 Authorization Key 以進行驗證。

### 取得專案代號
進入 SERVICE 管理介面，可以取得系統依據開發代碼(SID)所建立的專案代號(Service)，此為服務產生予用戶的預設值，專案代號具唯一性，建立後即不可變更。

### 建立資料架構
SCHEMA 管理介面，提供了於專案代號之下開發資料架構(Schema)的功能，SCHEMA 可實現 API 進行 PutEntry 以及 Query 操作時的介面定義，以及存儲於 Time Series Database 的資料架構。

### 透過 TSDBase API 上傳資料
TSDBase(Time Series Database) 透過自定 SCHEMA，提供軟體開發者一個能夠自由定義，具高擴充性、可靠、低延遲，並以 JSON format 儲存與讀取資料的資料儲存服務平台。
當 SCHEMA 建立完成，開發者便可立即透過 TSDBase API 進行資料的存儲查詢。

### 開放資料與審核
開發者經由 API 所存儲的資料，除了透過開發者自有之SID進行存儲外，亦可透過管理介面，審核同樣已申請開發憑證的開發者來開放讀取的權限。
相對的，開發者亦可透過申請頁面，送出 SCHEMA 的申請，供 API SCHEMA 擁有者進行審核。
