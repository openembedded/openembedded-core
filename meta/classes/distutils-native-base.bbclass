DEPENDS  += "${@["python-native", ""][(d.getVar('PACKAGES', True) == '')]}"

inherit distutils-common-base
