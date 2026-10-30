# Redstone Priority Calculator / 紅石優先級排列計算器

![Version](https://img.shields.io/badge/version-1.0.0-blue)
![License](https://img.shields.io/badge/license-MIT-green)
![Tech](https://img.shields.io/badge/stack-Fabric%20%2B%20Java%2017-lightgrey)
![Minecraft](https://img.shields.io/badge/Minecraft-1.20.4-orange)

[中文](#中文) | [English](#english)

---

## 中文

### 目錄
- [關於](#關於)
- [功能](#功能)
- [安裝](#安裝)
- [使用方式](#使用方式)
- [授權](#授權)

### 關於
Redstone Priority Calculator 是 Minecraft Java Edition 1.20.4 的 Fabric 客戶端模組, 用來產生並視覺化紅石研究標籤的排列順序. 作者: `YuYueSama`. 本模組只需安裝在客戶端, 伺服器不需要安裝.

結果是根據既有排列資料歸納出的規律模型, 不代表 Minecraft 引擎已證實的通用紅石優先級, 也不等同於遊戲實測結果.

### 功能
- 根據輸入元素產生排列, 顯示元素數量與排列總數.
- 點選排列後, 以由上而下的流程卡顯示執行順位, 元件標籤與延遲資訊.
- 排列清單與順位流程可獨立捲動.
- 支援複製目前排列與複製全部排列. 複製全部最多 2,000 筆.
- 內建研究標籤: `0` 代表比較器, `1` 至 `4` 代表標示 1 至 4 RT 的中繼器; 也可輸入自訂符號或名稱.

### 安裝
需求: Minecraft Java Edition 1.20.4, Fabric Loader, Fabric API, Java 17.

1. 安裝 Minecraft 1.20.4 對應的 Fabric Loader.
2. 下載相容於 Minecraft 1.20.4 的 Fabric API.
3. 將模組 JAR 與 Fabric API JAR 放入 `.minecraft/mods`.
4. 使用 Minecraft 1.20.4 的 Fabric 設定檔啟動遊戲.

從原始碼建置時, 在專案根目錄執行:

```powershell
.\gradlew.bat test build
```

Linux 或 macOS 可執行:

```bash
./gradlew test build
```

模組 JAR 會輸出至 `build/libs/`.

### 使用方式
在遊戲中按 `P` 開啟計算器. 可於 `選項 > 控制 > 按鍵綁定 > 紅石優先級計算器` 重新綁定快捷鍵.

輸入可使用連續 Unicode code point, 例如 `01234`, `ABCDE`; 也可使用逗號分隔名稱, 例如 `比較器,中繼器1,中繼器2`. 逗號分隔名稱會移除前後空白. 空名稱與重複名稱會顯示錯誤. 名稱本身目前不能包含逗號.

排列規則如下:
- 三個元素依序產生 `abc, bac, acb, cab, bca, cba`.
- 四個以上的元素從右側位置開始, 每次依反向輸入順序選取剩餘元素, 直到剩下三個元素, 再套用上述六種順序.
- 兩個元素依序輸出原順序與反向順序; 一個元素則輸出自身.

排列總數使用 `BigInteger` 計算. n 個互不重複的元素會產生 n! 筆排列. 為控制記憶體使用量, 單次最多產生 100,000 筆; 超過上限時會提示並停止計算.

本工具定義的模型不證明 Minecraft 內部排程規則. 遊戲實測結果需記錄 Minecraft 版本, 元件組合, 觸發方式, 相對位置與方向, 是否同時觸發, 以及觀察到的先後順序. 紅石事件順序可能因事件種類, 方塊更新, 排程 tick, 位置與觸發條件而異. 本工具不宣稱提供不分情境的全域元件排序.

### 授權
本專案採用 MIT License 授權, 詳見 [LICENSE](./LICENSE).

---

## English

### Table of Contents
- [About](#about)
- [Features](#features)
- [Installation](#installation)
- [Usage](#usage)
- [License](#license)

### About
Redstone Priority Calculator is a Fabric client-side mod for Minecraft Java Edition 1.20.4. It generates and visualizes permutations of redstone study labels. Author: `YuYueSama`. The server does not need the mod.

Results come from a model inferred from existing permutation data. They are not a universal redstone priority proven by the Minecraft engine and are not in-game measurements.

### Features
- Generate permutations from user input and display the element and permutation counts.
- View each selected permutation as a top-to-bottom execution flow with position, component label, and delay information.
- Scroll the permutation list and execution flow independently.
- Copy the selected permutation or all permutations. Copy-all is limited to 2,000 rows.
- Includes study labels: `0` for a comparator and `1` through `4` for repeaters labeled with 1 through 4 RT. Custom symbols and names are also supported.

### Installation
Requirements: Minecraft Java Edition 1.20.4, Fabric Loader, Fabric API, and Java 17.

1. Install Fabric Loader for Minecraft 1.20.4.
2. Download a Fabric API version compatible with Minecraft 1.20.4.
3. Place the mod JAR and Fabric API JAR in `.minecraft/mods`.
4. Launch the Fabric profile for Minecraft 1.20.4.

To build from source, run this command from the project root:

```bash
./gradlew test build
```

On Windows, run:

```powershell
.\gradlew.bat test build
```

The mod JAR is written to `build/libs/`.

### Usage
Press `P` in game to open the calculator. Rebind the key under `Options > Controls > Key Binds > Redstone Priority Calculator`.

Input may contain continuous Unicode code points, such as `01234`, `ABCDE`. Comma-separated names are also accepted, for example `Comparator,Repeater 1,Repeater 2`. Whitespace around comma-separated names is trimmed. Empty and duplicate names produce an error. Names cannot currently contain commas.

Permutation order:
- Three elements use `abc, bac, acb, cab, bca, cba`.
- With four or more elements, fill positions from the right by selecting remaining elements in reverse input order. Apply the six patterns above when three elements remain.
- Two elements produce the input order followed by the reverse order. One element produces itself.

The total is calculated with `BigInteger`. n unique elements produce n! permutations. To limit memory use, the mod generates at most 100,000 rows. Larger requests show a warning and stop.

The model does not prove Minecraft's internal scheduling rules. In-game measurements should record the Minecraft version, component combination, trigger method, relative position and direction, whether events were simultaneous, and the observed order. Redstone event order can depend on event type, block updates, scheduled ticks, position, and trigger conditions. This tool does not claim to provide a context-free global component ranking.

### License
This project is licensed under the MIT License. See [LICENSE](./LICENSE).
