DESCRIPTION = "Next generation package handling tool."
HOMEPAGE = "http://labix.org/smart/"
LICENSE = "GPL v2+"
DEPENDS = "zlib python desktop-file-utils-native python-pygtk rpm"

SRC_URI = "http://labix.org/download/smart/smart-1.1.tar.bz2"

S = "${WORKDIR}/smart-${PV}"

inherit distutils

FILES_${PN} += "/usr/share/lib/${PYTHON_DIR}/site-packages/smart/interfaces/images/*.png \
	        /usr/share/lib/${PYTHON_DIR/site-packages/smart/backends/"

FILES_${PN}-doc += "/usr/share/share/man/man8/smart.8"

FILES_${PN}-locale += "/usr/share/share/locale/es_ES/LC_MESSAGES/smart.mo \
		       /usr/share/share/locale/it/LC_MESSAGES/smart.mo \
		       /usr/share/share/locale/de/LC_MESSAGES/smart.mo \
		       /usr/share/share/locale/ru/LC_MESSAGES/smart.mo \
		       /usr/share/share/locale/sv/LC_MESSAGES/smart.mo \
		       /usr/share/share/locale/fr/LC_MESSAGES/smart.mo \
		       /usr/share/share/locale/hu/LC_MESSAGES/smart.mo \
		       /usr/share/share/locale/zh_TW/LC_MESSAGES/smart.mo \
		       /usr/share/share/locale/pt_BR/LC_MESSAGES/smart.mo \
		       /usr/share/share/locale/zh_CN/LC_MESSAGES/smart.mo"

FILES_${PN}-dbg += "/usr/lib/${PYTHON_DIR}/site-packages/smart/backends/rpm/.debug \
		    /usr/lib/${PYTHON_DIR}/site-packages/smart/backends/deb/.debug \"