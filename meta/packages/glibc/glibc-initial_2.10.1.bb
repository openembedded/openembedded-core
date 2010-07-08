require glibc_${PV}.bb
require glibc-initial.inc

do_configure_prepend () {
	unset CFLAGS
}
