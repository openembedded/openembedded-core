DEPENDS  += "${@["python-native python", ""][(d.getVar('PACKAGES', True) == '')]}"
RDEPENDS_${PN} += "${@['', 'python-core']['${CLASSOVERRIDE}' == 'class-target']}"

inherit distutils-common-base pythonnative

