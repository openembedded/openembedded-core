import os
import re
import subprocess
from oeqa.utils.httpserver import HTTPService

from oeqa.runtime.case import OERuntimeTestCase
from oeqa.core.decorator.depends import OETestDepends
from oeqa.core.decorator.oeid import OETestID
from oeqa.core.decorator.data import skipIfNotDataVar, skipIfNotFeature
from oeqa.runtime.decorator.package import OEHasPackage

class SmartTest(OERuntimeTestCase):

    def smart(self, command, expected = 0):
        command = 'smart %s' % command
        status, output = self.target.run(command, 1500)
        message = os.linesep.join([command, output])
        self.assertEqual(status, expected, message)
        self.assertFalse('Cannot allocate memory' in output, message)
        return output

class SmartBasicTest(SmartTest):

    @skipIfNotFeature('package-management',
                      'Test requires package-management to be in IMAGE_FEATURES')
    @skipIfNotDataVar('PACKAGE_CLASSES', 'package_rpm',
                      'RPM is not the primary package manager')
    @OEHasPackage(['smartpm'])
    @OETestID(716)
    @OETestDepends(['ssh.SSHTest.test_ssh'])
    def test_smart_help(self):
        self.smart('--help')

    @OETestID(968)
    @OETestDepends(['smart.SmartBasicTest.test_smart_help'])
    def test_smart_version(self):
        self.smart('--version')

    @OETestID(721)
    @OETestDepends(['smart.SmartBasicTest.test_smart_help'])
    def test_smart_info(self):
        self.smart('info python-smartpm')

    @OETestID(421)
    @OETestDepends(['smart.SmartBasicTest.test_smart_help'])
    def test_smart_query(self):
        self.smart('query python-smartpm')

    @OETestID(720)
    @OETestDepends(['smart.SmartBasicTest.test_smart_help'])
    def test_smart_search(self):
        self.smart('search python-smartpm')

    @OETestID(722)
    @OETestDepends(['smart.SmartBasicTest.test_smart_help'])
    def test_smart_stats(self):
        self.smart('stats')

class SmartRepoTest(SmartTest):

    @classmethod
    def setUpClass(cls):
        cls.repolist = []
        cls.repo_server = HTTPService(cls.tc.td['WORKDIR'],
                                      cls.tc.target.server_ip)
        cls.repo_server.start()

    @classmethod
    def tearDownClass(cls):
        cls.repo_server.stop()
        for repo in cls.repolist:
            cls.tc.target.run('smart channel -y --remove %s' % repo)

    @OETestID(1143)
    @OETestDepends(['smart.SmartBasicTest.test_smart_help'])
    def test_smart_channel(self):
        self.smart('channel', 1)

    @OETestID(719)
    @OETestDepends(['smart.SmartBasicTest.test_smart_help'])
    def test_smart_channel_add(self):
        image_pkgtype = self.tc.td['IMAGE_PKGTYPE']
        deploy_url = 'http://%s:%s/%s' % (self.target.server_ip,
                                          self.repo_server.port,
                                          image_pkgtype)
        pkgarchs = self.tc.td['PACKAGE_ARCHS'].replace("-","_").split()
        archs = os.listdir(os.path.join(self.repo_server.root_dir,
                                        image_pkgtype))
        for arch in archs:
            if arch in pkgarchs:
                cmd = ('channel -y --add {a} type=rpm-md '
                      'baseurl={u}/{a}'.format(a=arch, u=deploy_url))
                self.smart(cmd)
                self.repolist.append(arch)
        self.smart('update')

    @OETestID(969)
    @OETestDepends(['smart.SmartBasicTest.test_smart_help'])
    def test_smart_channel_help(self):
        self.smart('channel --help')

    @OETestID(970)
    @OETestDepends(['smart.SmartBasicTest.test_smart_help'])
    def test_smart_channel_list(self):
        self.smart('channel --list')

    @OETestID(971)
    @OETestDepends(['smart.SmartBasicTest.test_smart_help'])
    def test_smart_channel_show(self):
        self.smart('channel --show')

    @OETestID(717)
    @OETestDepends(['smart.SmartBasicTest.test_smart_help'])
    def test_smart_channel_rpmsys(self):
        self.smart('channel --show rpmsys')
        self.smart('channel --disable rpmsys')
        self.smart('channel --enable rpmsys')

    @OETestID(1144)
    @OETestDepends(['smart.SmartRepoTest.test_smart_channel_add'])
    def test_smart_install(self):
        self.smart('remove -y psplash-default')
        self.smart('install -y psplash-default')

    @OETestID(728)
    @OETestDepends(['smart.SmartRepoTest.test_smart_install'])
    def test_smart_install_dependency(self):
        self.smart('remove -y psplash')
        self.smart('install -y psplash-default')

    @OETestID(723)
    @OETestDepends(['smart.SmartRepoTest.test_smart_channel_add'])
    def test_smart_install_from_disk(self):
        self.smart('remove -y psplash-default')
        self.smart('download psplash-default')
        self.smart('install -y ./psplash-default*')

    @OETestID(725)
    @OETestDepends(['smart.SmartRepoTest.test_smart_channel_add'])
    def test_smart_install_from_http(self):
        output = self.smart('download --urls psplash-default')
        url = re.search('(http://.*/psplash-default.*\.rpm)', output)
        self.assertTrue(url, msg="Couln't find download url in %s" % output)
        self.smart('remove -y psplash-default')
        self.smart('install -y %s' % url.group(0))

    @OETestID(729)
    @OETestDepends(['smart.SmartRepoTest.test_smart_install'])
    def test_smart_reinstall(self):
        self.smart('reinstall -y psplash-default')

    @OETestID(727)
    @OETestDepends(['smart.SmartRepoTest.test_smart_channel_add'])
    def test_smart_remote_repo(self):
        self.smart('update')
        self.smart('install -y psplash')
        self.smart('remove -y psplash')

    @OETestID(726)
    @OETestDepends(['smart.SmartBasicTest.test_smart_help'])
    def test_smart_local_dir(self):
        self.target.run('mkdir /tmp/myrpmdir')
        self.smart('channel --add myrpmdir type=rpm-dir path=/tmp/myrpmdir -y')
        self.target.run('cd /tmp/myrpmdir')
        self.smart('download psplash')
        output = self.smart('channel --list')
        for i in output.split("\n"):
            if ("rpmsys" != str(i)) and ("myrpmdir" != str(i)):
                self.smart('channel --disable '+str(i))
        self.target.run('cd $HOME')
        self.smart('install psplash')
        for i in output.split("\n"):
            if ("rpmsys" != str(i)) and ("myrpmdir" != str(i)):
                self.smart('channel --enable '+str(i))
        self.smart('channel --remove myrpmdir -y')
        self.target.run("rm -rf /tmp/myrpmdir")

    @OETestID(718)
    @OETestDepends(['smart.SmartBasicTest.test_smart_help'])
    def test_smart_add_rpmdir(self):
        self.target.run('mkdir /tmp/myrpmdir')
        self.smart('channel --add myrpmdir type=rpm-dir path=/tmp/myrpmdir -y')
        self.smart('channel --disable myrpmdir -y')
        output = self.smart('channel --show myrpmdir')
        self.assertTrue("disabled = yes" in output, msg="Failed to disable rpm dir")
        self.smart('channel --enable  myrpmdir -y')
        output = self.smart('channel --show myrpmdir')
        self.assertFalse("disabled = yes" in output, msg="Failed to enable rpm dir")
        self.smart('channel --remove myrpmdir -y')
        self.target.run("rm -rf /tmp/myrpmdir")

    @OETestID(731)
    @OETestDepends(['smart.SmartRepoTest.test_smart_channel_add'])
    def test_smart_remove_package(self):
        self.smart('install -y psplash')
        self.smart('remove -y psplash')
