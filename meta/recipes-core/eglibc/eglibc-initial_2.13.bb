require eglibc_${PV}.bb
require eglibc-initial.inc

do_configure_prepend () {
        unset CFLAGS
}
