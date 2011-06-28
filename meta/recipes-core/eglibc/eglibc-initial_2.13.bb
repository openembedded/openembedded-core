require eglibc_${PV}.bb
require eglibc-initial.inc

do_install_locale() {
	:
}

do_configure_prepend () {
        unset CFLAGS
}
