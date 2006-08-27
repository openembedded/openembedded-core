require ipkg-utils_${PV}.bb
SRC_URI += "file://ipkg-utils-fix.patch;patch=1"

RDEPENDS = ""
PR = "r8"

inherit native

# Avoid circular dependencies from package_ipk.bbclass
PACKAGES = ""
FILESDIR = "${@os.path.dirname(bb.data.getVar('FILE',d,1))}/ipkg-utils"

do_stage() {
        for i in ${INSTALL}; do
                install -m 0755 $i ${STAGING_BINDIR}
        done
}
