require xorg-app-common.inc

DESCRIPTION = "point and click selection of X11 font names"
DEPENDS += " libxaw"
PR = "r1"

do_configure_prepend () {
	sed -i -e 's/XAW_CHECK_XPRINT_SUPPORT(\(.*\))/PKG_CHECK_MODULES(\1, xaw7)/' ${S}/configure.ac
}
