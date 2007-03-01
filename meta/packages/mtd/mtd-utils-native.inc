inherit native
DEPENDS = "zlib-native lzo-native"
FILESDIR = "${@os.path.dirname(bb.data.getVar('FILE',d,1))}/mtd-utils"

do_stage () {
        for binary in ${mtd_utils}; do
                install -m 0755 $binary ${STAGING_BINDIR}
        done
}
									