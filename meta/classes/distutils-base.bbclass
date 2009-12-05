DEPENDS  += "${@["python-native python", ""][(bb.data.getVar('PACKAGES', d, 1) == '')]}"
RDEPENDS += "python-core"

inherit distutils-common-base

