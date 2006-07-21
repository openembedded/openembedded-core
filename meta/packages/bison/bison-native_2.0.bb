include bison_${PV}.bb
SECTION = "devel"
FILESDIR = "${@os.path.dirname(bb.data.getVar('FILE',d,1))}/bison-${PV}"
S = "${WORKDIR}/bison-${PV}"
PR = "r2"

inherit native autotools

do_stage() {
	rm -f ${STAGING_BINDIR}/yacc
	rm -f ${STAGING_BINDIR}/bison
	install -m 0755 src/bison ${STAGING_BINDIR}/
	cat >${STAGING_BINDIR}/yacc <<EOF
#!/bin/sh
exec ${STAGING_BINDIR}/bison -y "\$@"
EOF
	chmod a+rx ${STAGING_BINDIR}/yacc
	install -d ${STAGING_BINDIR}/../share/bison/m4sugar
	install -m 0755 data/c.m4 data/glr.c data/lalr1.cc data/yacc.c ${STAGING_BINDIR}/../share/bison/
	install -m 0755 data/m4sugar/m4sugar.m4 ${STAGING_BINDIR}/../share/bison/m4sugar/
}
