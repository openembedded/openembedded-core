include ipkg-utils_${PV}.bb
SRC_URI += "file://ipkg-utils-fix.patch;patch=1"

RDEPENDS = ""
PR = "r7"

inherit native

# Avoid circular dependencies from package_ipk.bbclass
PACKAGES = ""

do_stage() {
        for i in ${INSTALL}; do
                install -m 0755 $i ${STAGING_BINDIR}
        done
}
