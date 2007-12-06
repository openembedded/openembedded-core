TEMPLATE = subdirs
CONFIG += ordered
!gtk-port:CONFIG += qt-port
qt-port:!win32-*:SUBDIRS += WebKit/qt/Plugins
SUBDIRS += \
        WebCore

qt-port:SUBDIRS += \
        WebKit/qt/QtLauncher
gtk-port:SUBDIRS += \
        WebKitTools/GtkLauncher
