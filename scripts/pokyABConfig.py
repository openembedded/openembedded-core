
import copy

builders = []

defaultenv = {}
defaultenv['POKYLIBC'] = 'glibc'
#defaultenv['PACKAGE_CLASSES'] = 'package_ipk package_deb'
#defaultenv['BB_NUMBER_THREADS'] = '4'
#defaultenv['DL_DIR'] = '/srv/poky/sources'

def runImage(factory, machine, image):
    defaultenv['MACHINE'] = machine
    factory.addStep(step.ShellCommand, description=["Building", machine, image], command=["./scripts/poky-autobuild", image], env=copy.copy(defaultenv), timeout=10000)

def runComplete(factory):
    factory.addStep(step.ShellCommand, description=["Mark", "complete"], command=["./scripts/poky-autobuild", "complete"], timeout=600)
            
def runPreamble(factory):
    factory.addStep(step.ShellCommand, description=["Run", "preamble"],  command=["./scripts/poky-autobuild", "preamble"], timeout=30)


from buildbot.process import step, factory
f3 = factory.BuildFactory()
f3.addStep(step.SVN, svnurl="http://svn.o-hand.com/repos/poky/trunk", mode="copy", timeout=10000)
runPreamble(f3)
runImage(f3, 'qemuarm', 'poky-image-minimal')
runImage(f3, 'qemuarm', 'poky-image-sato')
runImage(f3, 'qemuarm', 'poky-image-sdk')
runImage(f3, 'spitz', 'poky-image-minimal')
runImage(f3, 'spitz', 'poky-image-sato')
runImage(f3, 'spitz', 'poky-image-sdk')
runImage(f3, 'akita', 'poky-image-minimal')
runImage(f3, 'akita', 'poky-image-sato')
runImage(f3, 'c7x0', 'poky-image-minimal')
runImage(f3, 'c7x0', 'poky-image-sato')
runImage(f3, 'cm-x270', 'poky-image-minimal')
runImage(f3, 'cm-x270', 'poky-image-sato')
runImage(f3, 'em-x270', 'poky-image-minimal')
runImage(f3, 'em-x270', 'poky-image-sato')
runImage(f3, 'htcuniversal', 'poky-image-minimal')
runImage(f3, 'htcuniversal', 'poky-image-sato')
runImage(f3, 'mx31litekit', 'poky-image-minimal')
runImage(f3, 'mx31litekit', 'poky-image-sato')
runImage(f3, 'mx31phy', 'poky-image-minimal')
runImage(f3, 'mx31phy', 'poky-image-sato')
runImage(f3, 'mx31ads', 'poky-image-minimal')
runImage(f3, 'mx31ads', 'poky-image-sato')
runImage(f3, 'zylonite', 'poky-image-minimal')
runImage(f3, 'zylonite', 'poky-image-sato')
runImage(f3, 'nokia770', 'poky-image-minimal')
runImage(f3, 'nokia770', 'poky-image-sato')
runImage(f3, 'nokia800', 'poky-image-minimal')
runImage(f3, 'nokia800', 'poky-image-sato')
runImage(f3, 'nokia800', 'poky-image-sdk')
runImage(f3, 'fic-gta01', 'poky-image-minimal')
runImage(f3, 'fic-gta01', 'poky-image-sato')
runImage(f3, 'qemux86', 'poky-image-minimal')
runImage(f3, 'qemux86', 'poky-image-sato')
runImage(f3, 'qemux86', 'poky-image-sdk')
runImage(f3, 'bootcdx86', 'poky-image-sato-cd')
defaultenv['POKYLIBC'] = 'uclibc'
runImage(f3, 'cm-x270', 'poky-image-minimal-mtdutils')
defaultenv['POKYLIBC'] = 'glibc'
runComplete(f3)

from buildbot.process import step, factory
f4 = factory.BuildFactory()
f4.addStep(step.SVN, svnurl="http://svn.o-hand.com/repos/poky/trunk", timeout=10000)
runPreamble(f4)
f4.addStep(step.ShellCommand, description=["Cleaning", "previous", "images"],  command="/bin/rm build/tmp/deploy/images/poky-image* || /bin/true", timeout=600)
f4.addStep(step.ShellCommand, description=["Cleaning", "previous", "images", "step 2"],  command="/bin/rm build/tmp/deploy/images/rootfs* || /bin/true", timeout=600)
runImage(f4, 'qemuarm', 'poky-image-sdk')
runImage(f4, 'qemuarm', 'world')
runImage(f4, 'qemux86', 'poky-image-sdk')
runImage(f4, 'qemux86', 'world')
runImage(f4, 'akita', 'poky-image-sato')
runImage(f4, 'mx31phy', 'poky-image-sato')
runImage(f4, 'em-x270', 'poky-image-sato')
runImage(f4, 'fic-gta01', 'poky-image-sato')
runComplete(f4)

from buildbot.process import step, factory
f5 = factory.BuildFactory()
f5.addStep(step.SVN, svnurl="http://svn.o-hand.com/repos/poky/trunk", mode="copy", timeout=10000)
runPreamble(f5)
defaultenv['DISTRO'] = 'poky-bleeding'
runImage(f5, 'akita', 'poky-image-sato')
runImage(f5, 'qemuarm', 'poky-image-sato')
runComplete(f5)

from buildbot.process import step, factory
f6 = factory.BuildFactory()
f6.addStep(step.SVN, svnurl="http://svn.o-hand.com/repos/poky/trunk", mode="copy", timeout=10000)
runPreamble(f6)
defaultenv['DISTRO'] = 'poky'
runImage(f6, 'qemuarm', 'meta-toolchain')
runImage(f6, 'qemuarm', 'meta-toolchain-sdk')
runImage(f6, 'qemux86', 'meta-toolchain')
runImage(f6, 'qemux86', 'meta-toolchain-sdk')
runImage(f6, 'qemuarm', 'world -c checkuriall')
runComplete(f6)

#from buildbot.process import step, factory
#f7 = factory.BuildFactory()
#f7.addStep(step.SVN, svnurl="http://svn.o-hand.com/repos/poky/trunk", timeout=10000)
#runPreamble(f7)
#defaultenv['DISTRO'] = 'poky'
#runImage(f7, 'qemuarm', 'world')
#runImage(f7, 'qemux86', 'world')
#runComplete(f7)

b3 = {'name': "poky-full-shihtzu",
      'slavename': "shihtzu-autobuild",
      'builddir': "full-shihtzu",
      'factory': f3,
      }

b4 = {'name': "poky-incremental-shihtzu",
      'slavename': "shihtzu-autobuild",
      'builddir': "incremental-shihtzu",
      'factory': f4
      }

b5 = {'name': "poky-full-bleeding-shihtzu",
      'slavename': "shihtzu-autobuild",
      'builddir': "full-bleeding-shihtzu",
      'factory': f5
      }

b6 = {'name': "poky-toolchain-shihtzu",
      'slavename': "shihtzu-autobuild",
      'builddir': "toolchain-shihtzu",
      'factory': f6
      }

#b7 = {'name': "poky-incremental-world-shihtzu",
#      'slavename': "shihtzu-autobuild",
#      'builddir': "incremental-world-shihtzu",
#      'factory': f7
#      }

poky_builders = [b3, b4, b5, b6]

