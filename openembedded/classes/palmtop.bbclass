# basically a placeholder for something more fancy
# for now, just declare some things

inherit qmake

EXTRA_QMAKEVARS_POST_append = " DEFINES+=QWS LIBS+=-lqpe CONFIG+=qt LIBS-=-lstdc++ LIBS+=-lsupc++"

DEPENDS_prepend = "virtual/libqpe uicmoc-native "

FILES_${PN} = "${palmtopdir}"
