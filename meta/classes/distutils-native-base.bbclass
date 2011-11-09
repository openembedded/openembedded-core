DEPENDS  += "${@["python-native", ""][(d.getVar('PACKAGES', 1) == '')]}"

inherit distutils-common-base
