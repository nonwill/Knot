#include "dlgsettime.h"

#include <QKeyEvent>

#include "mainwindow.h"
#include "ui_dlgsettime.h"
#include "ui_mainwindow.h"
extern MainWindow* mw_one;
extern QString iniFile;
extern QRegularExpression regxNumber;
extern bool isBreak;

dlgSetTime::dlgSetTime(QWidget* parent)
    : QDialog(parent), ui(new Ui::dlgSetTime) {
  ui->setupUi(this);
  QFont font;
  font.setPixelSize(30);
  font.setBold(true);
  ui->editAmount->setFont(font);
  ui->btn0->setFont(font);
  ui->btn1->setFont(font);
  ui->btn2->setFont(font);
  ui->btn3->setFont(font);
  ui->btn4->setFont(font);
  ui->btn5->setFont(font);
  ui->btn6->setFont(font);
  ui->btn7->setFont(font);
  ui->btn8->setFont(font);
  ui->btn9->setFont(font);
  ui->btn0->setFont(font);
  ui->btnDot->setFont(font);
  ui->btnDel->setFont(font);

  QValidator* validator =
      new QRegularExpressionValidator(regxNumber, ui->editAmount);
  ui->editAmount->setValidator(validator);
  ui->editAmount->setPlaceholderText(tr("Number"));

  // ui->btnCustom->setCursor(Qt::ArrowCursor);
  // QWidgetAction* action = new QWidgetAction(ui->editDesc);
  // action->setDefaultWidget(ui->btnCustom);
  //  ui->editDesc->addAction(action, QLineEdit::TrailingPosition);
}

dlgSetTime::~dlgSetTime() { delete ui; }

void dlgSetTime::on_btnBack_clicked() { close(); }

void dlgSetTime::keyReleaseEvent(QKeyEvent* event) { event->accept(); }

void dlgSetTime::on_btnOk_clicked() {
  if (!mw_one->isAdd) {
    mw_one->set_Time();
  } else {
    if (mw_one->isTesting) {
      for (int i = 0; i < 500; i++)
        mw_one->add_Data(mw_one->get_tw(mw_one->ui->tabWidget->currentIndex()),
                         ui->timeEdit->text(), ui->editAmount->text().trimmed(),
                         ui->editDesc->text().trimmed());
    } else
      mw_one->add_Data(mw_one->get_tw(mw_one->ui->tabWidget->currentIndex()),
                       ui->timeEdit->text(), ui->editAmount->text().trimmed(),
                       ui->editDesc->text().trimmed());
  }

  // Save Desc Text
  QString str = ui->editDesc->text().trimmed();
  int count = mw_one->mydlgList->ui->listWidget->count();
  for (int i = 0; i < count; i++) {
    QString str1 = mw_one->mydlgList->ui->listWidget->item(i)->text().trimmed();
    if (str == str1) {
      mw_one->mydlgList->ui->listWidget->takeItem(i);
      break;
    }
  }

  if (str.length() > 0) mw_one->mydlgList->ui->listWidget->insertItem(0, str);

  close();
}

void dlgSetTime::on_btn7_clicked() { set_Amount("7"); }

void dlgSetTime::on_btn8_clicked() { set_Amount("8"); }

void dlgSetTime::on_btn9_clicked() { set_Amount("9"); }

void dlgSetTime::on_btn4_clicked() { set_Amount("4"); }

void dlgSetTime::on_btn5_clicked() { set_Amount("5"); }

void dlgSetTime::on_btn6_clicked() { set_Amount("6"); }

void dlgSetTime::on_btn1_clicked() { set_Amount("1"); }

void dlgSetTime::on_btn2_clicked() { set_Amount("2"); }

void dlgSetTime::on_btn3_clicked() { set_Amount("3"); }

void dlgSetTime::on_btn0_clicked() { set_Amount("0"); }

void dlgSetTime::on_btnDot_clicked() { set_Amount("."); }

void dlgSetTime::on_btnDel_clicked() {
  QString str = ui->editAmount->text().trimmed();
  str = str.mid(0, str.length() - 1);
  ui->editAmount->setText(str);
}

void dlgSetTime::set_Amount(QString Number) {
  QString str = ui->editAmount->text().trimmed();
  if (str == "0.00") ui->editAmount->setText("");
  if (str.split(".").count() == 2 && str != "0.00") {
    QString str0 = str.split(".").at(1);
    if (str0.length() == 2) return;
  }
  ui->editAmount->setText(str + Number);
}

void dlgSetTime::on_btnCustom_clicked() {
  if (mw_one->mydlgList->isHidden()) {
    mw_one->mydlgList->setModal(true);

    int h = mw_one->height() * 2 / 3;
    int y = ui->lblDesc->y() + ui->lblDesc->height() - h;
    mw_one->mydlgList->setGeometry(ui->editDesc->x(), y, ui->editDesc->width(),
                                   h);

    mw_one->mydlgList->show();
    mw_one->mydlgList->ui->listWidget->setFocus();
    if (mw_one->mydlgList->ui->listWidget->count() > 0)
      mw_one->mydlgList->ui->listWidget->setCurrentRow(0);
  }
}

void dlgSetTime::saveCustomDesc() {
  QSettings Reg(iniFile, QSettings::IniFormat);
  int count = mw_one->mydlgList->ui->listWidget->count();
  Reg.setValue("/CustomDesc/Count", count);
  QStringList list;
  for (int i = 0; i < count; i++) {
    if (isBreak) break;
    list.append(mw_one->mydlgList->ui->listWidget->item(i)->text().trimmed());
  }
  // list = QSet<QString>(list.begin(), list.end()).values(); //IOS无法编译通过
  removeDuplicates(&list);
  for (int i = 0; i < list.count(); i++) {
    if (isBreak) break;
    QString str = list.at(i);
    if (str.length() > 0)
      Reg.setValue("/CustomDesc/Item" + QString::number(i), str);
  }
}

int dlgSetTime::removeDuplicates(QStringList* that) {
  int n = that->size();
  int j = 0;
  QSet<QString> seen;
  seen.reserve(n);
  int setSize = 0;
  for (int i = 0; i < n; ++i) {
    const QString& s = that->at(i);
    seen.insert(s);
    if (setSize == seen.size())  // unchanged size => was already seen
      continue;
    ++setSize;
    // //将不重复项与重复项交换（新，IOS无法编译通过）
    // if (j != i) that->swapItemsAt(i, j);
    if (j != i) that->swap(i, j);
    ++j;
  }
  if (n != j) that->erase(that->begin() + j, that->end());
  return n - j;
}
