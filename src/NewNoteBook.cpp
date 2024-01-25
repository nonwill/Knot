#include "NewNoteBook.h"

#include "MainWindow.h"
#include "ui_NewNoteBook.h"

extern MainWindow* mw_one;
extern Method* m_Method;

NewNoteBook::NewNoteBook(QWidget* parent)
    : QDialog(parent), ui(new Ui::NewNoteBook) {
  ui->setupUi(this);
  setWindowFlag(Qt::FramelessWindowHint);
  ui->editName->installEventFilter(this);
  mw_one->set_ToolButtonStyle(this);

  QStringList list;
  list.append(tr("Main Root"));
  int count = mw_one->m_NotesList->ui->treeWidget->topLevelItemCount();
  for (int i = 0; i < count; i++) {
    list.append(mw_one->m_NotesList->ui->treeWidget->topLevelItem(i)
                    ->text(0)
                    .trimmed());
  }
  ui->cboxRoot->addItems(list);

  int x, y, w, h;
  w = mw_one->width() - 20;
  h = this->height();
  x = mw_one->geometry().x() + (mw_one->width() - w) / 2;
  y = 150;
  setGeometry(x, y, w, h);

  ui->editName->setFocus();

  m_Method->showGrayWindows();
  show();
  while (!isHidden()) QCoreApplication::processEvents();
}

NewNoteBook::~NewNoteBook() { delete ui; }

bool NewNoteBook::eventFilter(QObject* watch, QEvent* evn) {
  if (evn->type() == QEvent::KeyRelease) {
    QKeyEvent* keyEvent = static_cast<QKeyEvent*>(evn);
    if (keyEvent->key() == Qt::Key_Back) {
      on_btnCancel_clicked();
      return true;
    }

    if (watch == ui->editName) {
      if (keyEvent->key() == Qt::Key_Return) {
        on_btnOk_clicked();
        return true;
      }
    }
  }

  return QWidget::eventFilter(watch, evn);
}

void NewNoteBook::closeEvent(QCloseEvent* event) {
  Q_UNUSED(event)
  m_Method->closeGrayWindows();
}

void NewNoteBook::on_btnCancel_clicked() {
  isOk = false;
  rootIndex = 0;
  close();
}

void NewNoteBook::on_btnOk_clicked() {
  isOk = true;
  rootIndex = ui->cboxRoot->currentIndex();
  notebookName = ui->editName->text().trimmed();
  notebookRoot = ui->cboxRoot->currentText().trimmed();
  close();
}
