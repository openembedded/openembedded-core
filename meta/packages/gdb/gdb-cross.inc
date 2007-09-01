require gdb-common.inc

DEPENDS = "ncurses-native"

EXTRA_OEMAKE = "'SUBDIRS=intl mmalloc libiberty opcodes bfd sim gdb etc utils' LDFLAGS='${BUILD_LDFLAGS}'"

EXTRA_OECONF = "--with-curses --with-readline"

do_configure () {
# override this function to avoid the autoconf/automake/aclocal/autoheader
# calls for now
	(cd ${S} && gnu-configize) || die "failure in running gnu-configize"
	oe_runconf
}

do_stage () {
	oe_runmake install
}
