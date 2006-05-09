# this build class sets up qmake variables to
#   * build using the Qt Windowing System (QWS)
#   * use qt
#   * link against supc++ instead of stdc++  
#   * use threads, if requested via PALMTOP_USE_MULTITHREADED_QT = "yes"
# inherit this class to build programs against libqpe
# inherit opie if you want to build programs against libopie2
# don't override EXTRA_QMAKEVARS_POST, if you use inherit this class

inherit qmake

# special case for DISTRO = sharprom
CPP_SUPPORT_LIB = "LIBS-=-lstdc++ LIBS+=-lsupc++"
CPP_SUPPORT_LIB_sharprom = "LIBS-=-lstdc++"
EXTRA_QMAKEVARS_POST += "DEFINES+=QWS CONFIG+=qt ${CPP_SUPPORT_LIB}"
EXTRA_QMAKEVARS_POST += '${@base_conditional("PALMTOP_USE_MULTITHREADED_QT", "yes", "CONFIG+=thread", "CONFIG-=thread",d)}'
EXTRA_QMAKEVARS_POST += "${@["LIBS+=-lqpe ", ""][(bb.data.getVar('PN', d, 1) == 'libqpe-opie')]}"
DEPENDS_prepend = "${@["virtual/libqpe1 uicmoc-native ", ""][(bb.data.getVar('PN', d, 1) == 'libqpe-opie')]}"

FILES_${PN} = "${palmtopdir}"
