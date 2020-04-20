# PttMarvel
此專案是我開發用來看Ptt版的安卓App

個人採用MVVM架構，其中RecyclerView搭配DataBinding撰寫更有彈性。


主要使用到的Google官方庫androidx.lifecycle:lifecycle-extensions
其中包含ViewModel、LiveData、DataBinding等幫助構成MVVM架構的庫。

使用到的外部庫有JSpoon、RxJava以及Glide。
1. JSpoon用來將HTML網頁的解析，主要撰寫Selector來解析
2. 使用RxJava進行網頁請求，其中包含像是錯誤處理、夾帶Cookie的請求。
3. Glide能網路圖片以非同步的方式載入到文章頁面。


### 影片
[![Demo影片](https://img.youtube.com/vi/w7tstw2yJM4/0.jpg)](https://youtu.be/w7tstw2yJM4)  
