// SPDX-License-Identifier: GPL-3.0
        
pragma solidity >=0.4.22 <0.9.0;

// This import is automatically injected by Remix
import "remix_tests.sol"; 

// This import is required to use custom transaction context
// Although it may fail compilation in 'Solidity Compiler' plugin
// But it will work fine in 'Solidity Unit Testing' plugin
import "remix_accounts.sol";
import "../contracts/SimpleContract.sol";

// File name has to end with '_test.sol', this file can contain more than one testSuite contracts
contract SimpleContractTest {
   SimpleContract simpleContract;
   address acc0;
   address acc1;
   address acc2;

   function beforeAll() public {
       acc0 = TestsAccounts.getAccount(0); // owner
       acc1 = TestsAccounts.getAccount(1); // user1
       acc2 = TestsAccounts.getAccount(2); // user2
   }

   function beforeEach() public {
       simpleContract = new SimpleContract();
   }

   /// #value: 1000000000000000000
   /// Test payment functionality
   function testPayment() public payable {
       Assert.equal(msg.value, 1 ether, "Value should be 1 ether");
       
       // Проверяем начальный баланс
       Assert.equal(simpleContract.getContractBalance(), 0, "Initial balance should be 0");
       
       // Делаем платеж
       simpleContract.payForNothing{value: 1 ether}();
       
       // Проверяем баланс контракта
       Assert.equal(simpleContract.getContractBalance(), 1 ether, "Contract balance should be 1 ether");
       
       // Проверяем платеж в маппинге
       Assert.equal(simpleContract.getMyPayments(), 1 ether, "Payment mapping should show 1 ether");
   }

   /// Test contract ownership
   function testOwnership() public {
       Assert.equal(simpleContract.owner(), address(this), "Owner should be test contract");
   }
   
   /// Test get functions
   function testGetFunctions() public {
       // Проверяем начальный баланс
       Assert.equal(simpleContract.getContractBalance(), 0, "Initial balance should be 0");
       
       // Проверяем начальные платежи
       Assert.equal(simpleContract.getMyPayments(), 0, "Initial payments should be 0");
   }
}
    