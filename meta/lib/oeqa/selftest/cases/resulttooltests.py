import os
import sys
basepath = os.path.abspath(os.path.dirname(__file__) + '/../../../../../')
lib_path = basepath + '/scripts/lib'
sys.path = sys.path + [lib_path]
from resulttool.report import ResultsTextReport
from resulttool.regression import ResultsRegressionSelector, ResultsRegression
from resulttool.merge import ResultsMerge
from resulttool.store import ResultsGitStore
from resulttool.resultsutils import checkout_git_dir
from oeqa.selftest.case import OESelftestTestCase

class ResultToolTests(OESelftestTestCase):

    def test_report_can_aggregate_test_result(self):
        result_data = {'result': {'test1': {'status': 'PASSED'},
                                  'test2': {'status': 'PASSED'},
                                  'test3': {'status': 'FAILED'},
                                  'test4': {'status': 'ERROR'},
                                  'test5': {'status': 'SKIPPED'}}}
        report = ResultsTextReport()
        result_report = report.get_aggregated_test_result(None, result_data)
        self.assertTrue(result_report['passed'] == 2, msg="Passed count not correct:%s" % result_report['passed'])
        self.assertTrue(result_report['failed'] == 2, msg="Failed count not correct:%s" % result_report['failed'])
        self.assertTrue(result_report['skipped'] == 1, msg="Skipped count not correct:%s" % result_report['skipped'])

    def test_regression_can_get_regression_base_target_pair(self):
        base_results_data = {'base_result1': {'configuration': {"TEST_TYPE": "oeselftest",
                                                                "HOST": "centos-7"}},
                             'base_result2': {'configuration': {"TEST_TYPE": "oeselftest",
                                                                "HOST": "centos-7",
                                                                "MACHINE": "qemux86-64"}}}
        target_results_data = {'target_result1': {'configuration': {"TEST_TYPE": "oeselftest",
                                                                    "HOST": "centos-7"}},
                               'target_result2': {'configuration': {"TEST_TYPE": "oeselftest",
                                                                    "HOST": "centos-7",
                                                                    "MACHINE": "qemux86"}},
                               'target_result3': {'configuration': {"TEST_TYPE": "oeselftest",
                                                                    "HOST": "centos-7",
                                                                    "MACHINE": "qemux86-64"}}}
        regression = ResultsRegressionSelector()
        pair = regression.get_regression_base_target_pair(self.logger, base_results_data, target_results_data)
        self.assertTrue('target_result1' in pair['base_result1'], msg="Pair not correct:%s" % pair['base_result1'])
        self.assertTrue('target_result3' in pair['base_result2'], msg="Pair not correct:%s" % pair['base_result2'])

    def test_regrresion_can_get_regression_result(self):
        base_result_data = {'result': {'test1': {'status': 'PASSED'},
                                       'test2': {'status': 'PASSED'},
                                       'test3': {'status': 'FAILED'},
                                       'test4': {'status': 'ERROR'},
                                       'test5': {'status': 'SKIPPED'}}}
        target_result_data = {'result': {'test1': {'status': 'PASSED'},
                                         'test2': {'status': 'FAILED'},
                                         'test3': {'status': 'PASSED'},
                                         'test4': {'status': 'ERROR'},
                                         'test5': {'status': 'SKIPPED'}}}
        regression = ResultsRegression()
        result = regression.get_regression_result(self.logger, base_result_data, target_result_data)
        self.assertTrue(result['test2']['base'] == 'PASSED',
                        msg="regression not correct:%s" % result['test2']['base'])
        self.assertTrue(result['test2']['target'] == 'FAILED',
                        msg="regression not correct:%s" % result['test2']['target'])
        self.assertTrue(result['test3']['base'] == 'FAILED',
                        msg="regression not correct:%s" % result['test3']['base'])
        self.assertTrue(result['test3']['target'] == 'PASSED',
                        msg="regression not correct:%s" % result['test3']['target'])

    def test_merge_can_merged_results(self):
        base_results_data = {'base_result1': {},
                             'base_result2': {}}
        target_results_data = {'target_result1': {},
                               'target_result2': {},
                               'target_result3': {}}

        merge = ResultsMerge()
        results = merge.merge_results(base_results_data, target_results_data)
        self.assertTrue(len(results.keys()) == 5, msg="merge not correct:%s" % len(results.keys()))

    def test_store_can_store_to_new_git_repository(self):
        basepath = os.path.abspath(os.path.dirname(__file__) + '/../../')
        source_dir = basepath + '/files/testresults'
        git_branch = 'qa-cycle-2.7'
        store = ResultsGitStore()
        output_dir = store.store_to_new(self.logger, source_dir, git_branch)
        self.assertTrue(checkout_git_dir(output_dir, git_branch), msg="store to new git repository failed:%s" %
                                                                      output_dir)
        store._remove_temporary_workspace_dir(output_dir)

    def test_store_can_store_to_existing(self):
        basepath = os.path.abspath(os.path.dirname(__file__) + '/../../')
        source_dir = basepath + '/files/testresults'
        git_branch = 'qa-cycle-2.6'
        store = ResultsGitStore()
        output_dir = store.store_to_new(self.logger, source_dir, git_branch)
        self.assertTrue(checkout_git_dir(output_dir, git_branch), msg="store to new git repository failed:%s" %
                                                                      output_dir)
        git_branch = 'qa-cycle-2.7'
        output_dir = store.store_to_existing_with_new_branch(self.logger, source_dir, output_dir, git_branch)
        self.assertTrue(checkout_git_dir(output_dir, git_branch), msg="store to existing git repository failed:%s" %
                                                                      output_dir)
        output_dir = store.store_to_existing(self.logger, source_dir, output_dir, git_branch)
        self.assertTrue(checkout_git_dir(output_dir, git_branch), msg="store to existing git repository failed:%s" %
                                                                      output_dir)
        store._remove_temporary_workspace_dir(output_dir)
