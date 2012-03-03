DEPENDS  += "${@["python-native python", ""][(d.getVar('PACKAGES', True) == '')]}"
RDEPENDS_${PN} += "${@['', 'python-core']['${PN}' == '${BPN}']}"

inherit distutils-common-base

