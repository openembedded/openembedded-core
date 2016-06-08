require python-git.inc

SRC_URI[md5sum] = "d92d96a8da0fc77cf141d3e16084e094"
SRC_URI[sha256sum] = "85de72556781480a38897a77de5b458ae3838b0fd589593679a1b5f34d181d84"

DEPENDS = "python3-gitdb"

inherit setuptools3

RDEPENDS_${PN} += "python3-gitdb python3-lang python3-io python3-shell python3-math python3-re python3-subprocess python3-stringold python3-unixadmin"
