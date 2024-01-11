import QtQuick 2.15
import QtQuick.Window 2.15
import QtQml 2.15
import QtQuick.Controls 2.15
import QtQuick.Layouts 1.15

Rectangle {
    id: root

    width: 500
    height: 400

    color: isDark ? "black" : "white"

    property int iconW: 18
    property int rowSpace: 3
    property int i: 0
    property int itemCount: 0
    property bool isHighPriority: false

    function setItemHeight(h) {}

    function gotoEnd() {
        view.positionViewAtEnd()
    }

    function gotoBeginning() {
        view.positionViewAtBeginning()
    }

    function gotoIndex(index) {
        view.positionViewAtIndex(index, Tumbler.Center)
    }

    function setHighPriority(isFalse) {
        isHighPriority = isFalse
    }

    function setCurrentItem(currentIndex) {
        view.currentIndex = currentIndex
    }

    function getCurrentIndex() {
        return view.currentIndex
    }

    function getItemCount() {
        itemCount = view.count
        console.log("count=" + itemCount)
        return itemCount
    }

    function getItemText(itemIndex) {
        var data = view.model.get(itemIndex)
        return data.time + "|=|" + data.dototext
    }

    function getText0(itemIndex) {
        var data = view.model.get(itemIndex)
        return data.text0
    }

    function getText1(itemIndex) {
        var data = view.model.get(itemIndex)
        return data.text1
    }

    function getText2(itemIndex) {
        var data = view.model.get(itemIndex)
        return data.text2
    }

    function getText3(itemIndex) {
        var data = view.model.get(itemIndex)
        return data.text3
    }

    function getTop(itemIndex) {
        var data = view.model.get(itemIndex)
        return data.text_top
    }

    function getType(itemIndex) {
        var data = view.model.get(itemIndex)
        return data.type
    }

    function addItem(t0, t1, t2, t3, height) {
        view.model.append({
                              "text0": t0,
                              "text1": t1,
                              "text2": t2,
                              "text3": t3,
                              "myh": height
                          })
    }

    function insertItem(text0, text1, text2, text3, curIndex) {
        view.model.insert(curIndex, {
                              "text0": text0,
                              "text1": text1,
                              "text2": text2,
                              "text3": text3
                          })
    }

    function delItem(currentIndex) {
        view.model.remove(currentIndex)
    }

    function modifyItem(currentIndex, strTime, strText) {

        view.model.setProperty(currentIndex, "time", strTime)
        view.model.setProperty(currentIndex, "dototext", strText)
    }

    function modifyItemTime(currentIndex, strTime) {

        view.model.setProperty(currentIndex, "time", strTime)
    }

    function modifyItemType(currentIndex, type) {

        view.model.setProperty(currentIndex, "type", type)
    }

    function modifyItemText0(currentIndex, strText) {
        view.model.setProperty(currentIndex, "text0", strText)
    }

    function modifyItemText2(currentIndex, strText) {
        view.model.setProperty(currentIndex, "text2", strText)
    }

    Component {
        id: dragDelegate

        Rectangle {
            id: listItem
            width: ListView.view.width
            height: getItemHeight() + 25
            color: ListView.isCurrentItem ? "lightblue" : "#ffffff" //选中颜色设置 #94caf7

            border.width: 1
            border.color: "lightgray" //"lightsteelblue"

            radius: 6

            function getItemHeight() {
                var item0H
                var item1H
                var item2H
                var item3H

                if (item0.text.length == 0)
                    item0H = 0
                else
                    item0H = item0.contentHeight

                if (item1.text.length == 0)
                    item1H = 0
                else
                    item1H = item1.contentHeight

                if (item2.text.length == 0)
                    item2H = 0
                else
                    item2H = item2.contentHeight

                if (item3.text.length == 0)
                    item3H = 0
                else
                    item3H = item3.contentHeight

                return item0H + item1H + item2H + item3H
            }

            RowLayout {

                id: idlistElemnet
                height: parent.height
                width: parent.width
                spacing: 2
                Layout.fillWidth: true

                Rectangle {
                    height: parent.height - 2
                    width: 6
                    radius: 2
                    anchors.leftMargin: 1
                    color: "red"
                    visible: false // item2.text.length ? true : false
                    Text {
                        anchors.centerIn: parent
                    }
                }

                ColumnLayout {
                    id: colLayout
                    height: parent.height
                    width: parent.width
                    spacing: 2
                    Layout.fillWidth: true
                    anchors.leftMargin: 0
                    anchors.rightMargin: 0

                    RowLayout {

                        id: row0

                        Image {
                            id: item0Img

                            width: iconW
                            height: item0.contentHeight
                            fillMode: Image.NoOption
                            horizontalAlignment: Image.AlignHCenter
                            verticalAlignment: Image.AlignVCenter

                            smooth: true
                            sourceSize.height: iconW
                            sourceSize.width: iconW
                            source: "/res/time.svg"

                            visible: false
                        }

                        Text {
                            id: item0

                            width: parent.width
                            Layout.preferredWidth: listItem.width - iconW - 3
                            Layout.alignment: Qt.AlignHCenter
                            horizontalAlignment: Text.AlignLeft
                            verticalAlignment: Text.AlignVCenter
                            wrapMode: TextArea.WordWrap
                            font.bold: true
                            text: text0

                            leftPadding: 5
                            rightPadding: 5
                        }
                    }

                    RowLayout {

                        id: row1
                        Layout.margins: rowSpace
                        visible: item1.text.length ? true : false

                        Image {
                            id: item1Img

                            width: iconW
                            height: parent.height - 2
                            fillMode: Image.NoOption
                            horizontalAlignment: Image.AlignHCenter
                            verticalAlignment: Image.AlignVCenter

                            smooth: true
                            sourceSize.height: iconW
                            sourceSize.width: iconW

                            source: "/res/je.svg"

                            visible: item1.text.length ? true : false
                        }

                        Rectangle {
                            height: item1.contentHeight
                            width: 6
                            radius: 2
                            anchors.leftMargin: 1
                            color: "red"
                            visible: false // item1.text.length ? true : false
                        }

                        Text {
                            id: item1
                            Layout.preferredWidth: listItem.width - iconW - rowSpace - 3

                            Layout.alignment: Qt.AlignHCenter
                            horizontalAlignment: Text.AlignLeft
                            verticalAlignment: Text.AlignVCenter

                            width: parent.width
                            wrapMode: TextArea.WrapAnywhere
                            color: "#13227a"
                            font.bold: false
                            text: text1

                            leftPadding: 5
                            rightPadding: 5

                            visible: item1.text.length ? true : false
                        }
                    }

                    RowLayout {

                        id: row2
                        Layout.margins: rowSpace
                        visible: item2.text.length ? true : false

                        Image {
                            id: item2Img

                            width: iconW
                            height: parent.height - 2
                            fillMode: Image.NoOption
                            horizontalAlignment: Image.AlignHCenter
                            verticalAlignment: Image.AlignVCenter

                            smooth: true
                            sourceSize.height: iconW
                            sourceSize.width: iconW
                            source: "/res/fl.svg"

                            visible: item2.text.length ? true : false
                        }

                        Rectangle {
                            height: item2.contentHeight
                            width: 6
                            radius: 2
                            anchors.leftMargin: 1
                            color: "green"
                            visible: false // item2.text.length ? true : false
                        }

                        Text {
                            id: item2
                            anchors.rightMargin: 0
                            Layout.preferredWidth: listItem.width - iconW - rowSpace - 3
                            Layout.alignment: Qt.AlignHCenter

                            horizontalAlignment: Text.AlignLeft
                            width: parent.width
                            wrapMode: TextArea.WordWrap
                            color: "#020202"
                            font.bold: false
                            text: text2

                            leftPadding: 5
                            rightPadding: 5

                            visible: item2.text.length ? true : false
                        }
                    }

                    RowLayout {

                        id: row3
                        Layout.margins: rowSpace
                        visible: item3.text.length ? true : false

                        Image {
                            id: item3Img

                            width: iconW
                            height: parent.height - 2
                            fillMode: Image.NoOption
                            horizontalAlignment: Image.AlignHCenter
                            verticalAlignment: Image.AlignVCenter

                            smooth: true
                            sourceSize.height: iconW
                            sourceSize.width: iconW
                            source: "/res/xq.svg"

                            visible: item3.text.length ? true : false
                        }

                        Rectangle {
                            height: item3.height
                            width: 6
                            radius: 2
                            anchors.leftMargin: 1
                            color: "blue"
                            visible: false // item3.text.length ? true : false
                        }

                        Text {
                            id: item3
                            Layout.preferredWidth: listItem.width - iconW - rowSpace - 3

                            Layout.alignment: Qt.AlignHCenter
                            horizontalAlignment: Text.AlignLeft
                            verticalAlignment: Text.AlignVCenter

                            width: parent.width
                            wrapMode: TextArea.WordWrap
                            color: "#515151"
                            font.bold: false
                            text: text3

                            leftPadding: 5
                            rightPadding: 5

                            visible: item3.text.length ? true : false
                        }
                    }
                }
            }

            MouseArea {

                property point clickPos: "0,0"

                anchors.fill: parent
                onPressed: {
                    clickPos = Qt.point(mouse.x, mouse.y)
                }
                onReleased: {
                    var delta = Qt.point(mouse.x - clickPos.x,
                                         mouse.y - clickPos.y)
                    console.debug("delta.x: " + delta.x)
                    if ((delta.x < 0) && (aBtnShow.running === false)
                            && (delBtn.width == 0)) {
                        aBtnShow.start()
                    } else if (aBtnHide.running === false
                               && (delBtn.width > 0)) {
                        aBtnHide.start()
                    }
                }

                onClicked: {

                    view.currentIndex = index //实现item切换

                    m_Method.clickMainEventData()
                }

                onPressAndHold: {

                    //m_Method.showNotsListMenu(mouse.x, mouse.y)
                }

                onDoubleClicked: {
                    m_Method.reeditMainEventData()

                    //mw_one.reeditData()
                    //var data = view.model.get(view.currentIndex)
                    //console.log(data.text0 + "," + data.type + ", count=" + view.count)
                }
            }

            Rectangle {
                color: "#AAAAAA"
                height: 0
                width: parent.width
                anchors.bottom: parent.bottom
            }

            Rectangle {
                id: delBtn
                visible: false
                height: parent.height
                width: 0
                color: "#FF0000"

                anchors.right: parent.right
                anchors.rightMargin: -30
                radius: 0

                Text {
                    width: 56
                    anchors.centerIn: parent

                    text: qsTr("Done")
                    color: "#ffffff"
                }

                MouseArea {
                    anchors.fill: parent
                    onClicked: {
                        m_Todo.addToRecycle()
                        view.model.remove(index)
                    }
                }
            }

            PropertyAnimation {
                id: aBtnShow
                target: delBtn
                property: "width"
                duration: 100
                from: 0
                to: 80
            }
            PropertyAnimation {
                id: aBtnHide
                target: delBtn
                property: "width"
                duration: 100
                from: 80
                to: 0
            }
        }
    }

    ListView {
        id: view

        anchors {
            fill: parent
            margins: 4
        }

        model: ListModel {
            id: listmain

            // debug
            ListElement {
                text0: '<span style="background-color: #ff6600;">Hello</span>'
                text1: "123456  <b>Hello</b> <i>World!</i>  123456"
                text2: '123456 <font color="red"><b>TEST</b></font>  123456'
                text3: "str3 1234567890 1234567890  1234567890 1234567890"
                myh: 0
            }
        }
        delegate: dragDelegate

        spacing: 4
        cacheBuffer: 50

        ScrollBar.vertical: ScrollBar {
            width: 8
            policy: ScrollBar.AsNeeded
        }
    }

    function getListEleHeadColor(ntype) {
        switch (ntype) {
        case 0:
            return "lightgray"
        case 1:
            return "red"
        case 2:
            return "yellow"
        case 3:
            return "lightblue"
        default:
            return "black"
        }
    }
}
