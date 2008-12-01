DEPENDS_prepend = "${@["qt4x11 ", ""][(bb.data.getVar('PN', d, 1) == 'qt4-x11-free')]}"

inherit qmake2
