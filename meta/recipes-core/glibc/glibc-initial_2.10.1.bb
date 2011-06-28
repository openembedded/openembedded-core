require glibc_${PV}.bb
require glibc-initial.inc

do_install_locale() {
	:
}

do_configure_prepend () {
	unset CFLAGS
}
