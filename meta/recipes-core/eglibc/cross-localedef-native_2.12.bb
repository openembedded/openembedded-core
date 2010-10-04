DESCRIPTION = "Cross locale generation tool for eglibc"
HOMEPAGE = "http://www.eglibc.org/home"
SECTION = "libs"
PRIORITY = "required"
LICENSE = "LGPL"

inherit native
inherit autotools

PR = "r0"
SRCREV="10809"
EGLIBC_BRANCH="eglibc-2_12"
SRC_URI = "svn://www.eglibc.org/svn/branches/;module=${EGLIBC_BRANCH};proto=http "
S = "${WORKDIR}/${EGLIBC_BRANCH}/localedef"

do_unpack_append() {
	bb.build.exec_func('do_move_ports', d)
}

do_move_ports() {
        if test -d ${WORKDIR}/${EGLIBC_BRANCH}/ports ; then
	    rm -rf ${S}../libc/ports
	    mv ${WORKDIR}/${EGLIBC_BRANCH}/ports ${S}/../libc/
	fi
}

EXTRA_OECONF = "--with-glibc=${WORKDIR}/${EGLIBC_BRANCH}/libc"

do_configure () {
	./configure ${EXTRA_OECONF}
}


do_install() {
	install -d ${D}${bindir} 
	install -m 0755 ${S}/localedef ${D}${bindir}/cross-localedef
}
