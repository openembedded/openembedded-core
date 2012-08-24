# Copyright (C) 2012 Khem Raj <raj.khem@gmail.com>
# Released under the MIT license (see COPYING.MIT for the terms)

require kmod.inc
inherit native

PR = "${INC_PR}.1"
SRC_URI += "file://fix-undefined-O_CLOEXEC.patch"

do_install_append (){
	for tool in depmod insmod lsmod modinfo modprobe rmmod
	do
		ln -s kmod ${D}${bindir}/$tool
	done
}
