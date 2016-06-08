require python-git.inc

SRC_URI[md5sum] = "f4f60f269fb5f6a332c653a5bc015239"
SRC_URI[sha256sum] = "20f3c90fb8a11edc52d363364fb0a116a410c7b7bdee24a433712b5413d1028e"

DEPENDS = "python-gitdb"

inherit setuptools

RDEPENDS_${PN} += "python-gitdb python-lang python-io python-shell python-math python-re python-subprocess python-stringold python-unixadmin"
