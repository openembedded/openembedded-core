require tasks.inc

LIC_FILES_CHKSUM = "file://COPYING;md5=94d55d512a9ba36caa9b7df079bae19f \
                    file://src/gtk/main.c;endline=19;md5=8659d0b7fd68a2ad6ac30c6539ea5b82 \
                    file://src/omoko/openmoko-tasks.c;endline=20;md5=04d56a46863c9f4247694f40257a836a \
                    file://src/hildon/hildon-tasks.c;endline=21;md5=488ddf31dc14b2196dec7cc736211b52"

SRC_URI = "http://pimlico-project.org/sources/${PN}/${PN}-${PV}.tar.gz \
           file://fix_pre-GTK+2.16.patch;apply=yes \
           "

OWL_poky = "--with-owl"

PR = "r0"
