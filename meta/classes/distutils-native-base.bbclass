DEPENDS  += "${@["python-native", ""][(bb.data.getVar('PACKAGES', d, 1) == '')]}"

inherit distutils-common-base
