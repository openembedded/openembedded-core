DEPENDS  += "${@["python-native python", ""][(d.getVar('PACKAGES', 1) == '')]}"
RDEPENDS_${PN} += "${@['', 'python-core']['${PN}' == '${BPN}']}"

inherit distutils-common-base

