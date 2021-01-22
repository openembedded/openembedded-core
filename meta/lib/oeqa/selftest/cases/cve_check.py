from oe.cve_check import Version
from oeqa.selftest.case import OESelftestTestCase

class CVECheck(OESelftestTestCase):

    def test_version_compare(self):
        result = Version("100") > Version("99")
        self.assertTrue( result, msg="Failed to compare version '100' > '99'")
        result = Version("2.3.1") > Version("2.2.3")
        self.assertTrue( result, msg="Failed to compare version '2.3.1' > '2.2.3'")
        result = Version("2021-01-21") > Version("2020-12-25")
        self.assertTrue( result, msg="Failed to compare version '2021-01-21' > '2020-12-25'")
        result = Version("1.2-20200910") < Version("1.2-20200920")
        self.assertTrue( result, msg="Failed to compare version '1.2-20200910' < '1.2-20200920'")

        result = Version("1.0") >= Version("1.0beta")
        self.assertTrue( result, msg="Failed to compare version '1.0' >= '1.0beta'")
        result = Version("1.0-rc2") > Version("1.0-rc1")
        self.assertTrue( result, msg="Failed to compare version '1.0-rc2' > '1.0-rc1'")
        result = Version("1.0.alpha1") < Version("1.0")
        self.assertTrue( result, msg="Failed to compare version '1.0.alpha1' < '1.0'")
        result = Version("1.0_dev") <= Version("1.0")
        self.assertTrue( result, msg="Failed to compare version '1.0_dev' <= '1.0'")

        # ignore "p1" and "p2", so these should be equal
        result = Version("1.0p2") <= Version("1.0p1") and Version("1.0p2") >= Version("1.0p1")
        self.assertTrue( result ,msg="Failed to compare version '1.0p2' to '1.0p1'")
