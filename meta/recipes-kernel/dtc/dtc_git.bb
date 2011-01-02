require dtc.inc
require dtc_git.inc
LICENSE="GPLv2|BSD"
LIC_FILES_CHKSUM = "file://GPL;md5=94d55d512a9ba36caa9b7df079bae19f \
		    file://libfdt/libfdt.h;beginline=3;endline=52;md5=fb360963151f8ec2d6c06b055bcbb68c"

SRC_URI += " file://remove_space_opt.patch"
