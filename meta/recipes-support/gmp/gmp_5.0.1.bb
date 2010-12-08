require gmp.inc
LICENSE="LGPLv3&GPLv3"
LIC_FILES_CHKSUM = "file://COPYING;md5=d32239bcb673463ab874e80d47fae504 \
		    file://version.c;endline=18;md5=d8c56b52b9092346b9f93b4da65ef790"
PR = "r0"

SRC_URI_append += "file://sh4-asmfix.patch \
                   file://use-includedir.patch "


SRC_URI[md5sum] = "6bac6df75c192a13419dfd71d19240a7"
SRC_URI[sha256sum] = "a2a610f01fd3298dc08c87bf30498c2402590e1bcb227fc40b15ee6d280939fb"
