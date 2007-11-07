inherit native
DEPENDS = "zlib-native lzo-native"
FILESDIR = "${@os.path.dirname(bb.data.getVar('FILE',d,1))}/mtd-utils"

do_stage () {
        install -d ${STAGING_INCDIR}/mtd
        for f in ${S}/include/mtd/*.h; do
                install -m 0644 $f ${STAGING_INCDIR}/mtd/
        done
        for binary in ${mtd_utils}; do
                install -m 0755 $binary ${STAGING_BINDIR}
        done
}

