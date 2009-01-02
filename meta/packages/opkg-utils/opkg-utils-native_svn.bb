require opkg-utils_svn.bb

RDEPENDS = ""

inherit native

# Avoid circular dependencies from package_ipk.bbclass
PACKAGES = ""

