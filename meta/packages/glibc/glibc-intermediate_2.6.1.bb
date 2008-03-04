require glibc_${PV}.bb
require glibc-intermediate.inc

# gcc uses -Werror which break on a "you have no thumb interwork" _warning_
do_configure_prepend() {
	sed -i s:-Werror:: ${S}/configure
}

