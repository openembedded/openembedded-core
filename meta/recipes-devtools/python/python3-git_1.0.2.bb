require python-git.inc

DEPENDS = "python3-gitdb"

inherit setuptools3

RDEPENDS_${PN} += "python3-gitdb python3-lang python3-io python3-shell python3-math python3-re python3-subprocess python3-stringold python3-unixadmin"
