require xorg-app-common.inc
PE = "1"
PR = "r1"

DEPENDS += " libxaw libxxf86vm libxt"

do_configure_prepend () {
	sed -i -e 's/XAW_CHECK_XPRINT_SUPPORT(\(.*\))/PKG_CHECK_MODULES(\1, xaw7)/' ${S}/configure.ac
}
