DEPENDS  += "${@["python-native python", ""][(bb.data.getVar('PACKAGES', d, 1) == '')]}"
RDEPENDS_${PN} += "${@['', 'python-core']['${PN}' == '${BPN}']}"

inherit distutils-common-base

