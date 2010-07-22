require xorg-app-common.inc

DESCRIPTION = "magnify parts of the screen"
DEPENDS += " libxaw libxt"
PE = "1"
PR = "r1"

do_configure_prepend () {
	sed -i -e 's/XAW_CHECK_XPRINT_SUPPORT(\(.*\))/PKG_CHECK_MODULES(\1, xaw7)/' ${S}/configure.ac
}
