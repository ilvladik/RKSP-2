// SPDX-License-Identifier: MIT
pragma solidity ^0.8.25;

contract SimpleContract {
  address public owner;
  mapping (address => uint256) public payments;
  
  event PaymentReceived(address indexed payer, uint amount);
  event WithdrawalMade(uint amount);

  constructor() {
    owner = msg.sender;
  }

  function payForNothing() public payable {
    require(msg.value > 0, "Payment amount must be greater than 0");
        // Складываем новый платеж с предыдущей суммой
    payments[msg.sender] += msg.value;
        
    emit PaymentReceived(msg.sender, msg.value);
  }

  function withdrawAll() public {
    require(msg.sender == owner, "Only owner can withdraw");
    require(address(this).balance > 0, "No balance to withdraw");
        
    uint amount = address(this).balance;
    address payable _to = payable(owner);
    _to.transfer(amount);
        
    emit WithdrawalMade(amount);
  }

  function getContractBalance() public view returns (uint) {
    return address(this).balance;
  }
    
    // Функция для просмотра суммы всех платежей конкретного адреса
  function getMyPayments() public view returns (uint) {
    return payments[msg.sender];
  }
}
      