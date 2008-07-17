require opkg-utils_svn.bb

RDEPENDS = ""

inherit native

# Avoid circular dependencies from package_ipk.bbclass
PACKAGES = ""
FILESDIR = "${@os.path.dirname(bb.data.getVar('FILE',d,1))}/opkg-utils"

