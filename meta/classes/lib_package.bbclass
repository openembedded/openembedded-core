#
# ${PN}-bin is defined in bitbake.conf
#
# We need to allow the other packages to be greedy with what they
# want out of /bin and /usr/bin before ${PN}-bin gets greedy.
# 
PACKAGES = "${PN}-dbg ${PN}-staticdev ${PN}-dev ${PN}-doc ${PN}-locale ${PN}-bin ${PN}"

